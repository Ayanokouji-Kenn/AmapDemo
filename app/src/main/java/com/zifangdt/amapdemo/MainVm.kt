package com.zifangdt.amapdemo

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import com.amap.api.maps.AMap
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.maps.model.MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils

class MainVm(app: Application) : AndroidViewModel(app) {


    fun initMyLocation(aMap: com.amap.api.maps.AMap) {
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。
        // （1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。

        aMap.myLocationStyle = MyLocationStyle().apply {
            interval(2000)    //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
            myLocationType(LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
        }
        //设置定位蓝点的Style
        aMap.uiSettings.isMyLocationButtonEnabled = true//;设置默认定位按钮是否显示，非必需设置。
        aMap.isMyLocationEnabled = true;// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setOnMyLocationChangeListener {
            LogUtils.d("zfdt===${it.longitude},${it.latitude}")
        }
    }

    private val mStartPoint = LatLonPoint(31.26, 120.731458)//起点，39.942295,116.335891
    private val mEndPoint = LatLonPoint(31.3, 120.764)//终点，39.995576,116.481288
    private val wayToPointList = mutableListOf<LatLonPoint>().apply {
        add(LatLonPoint(31.288204, 120.777635))
    }// 途经点

    fun calcRoute(context: Context, aMap: AMap) {
        val routeSearch = RouteSearch(context)
        val fromAndTo = RouteSearch.FromAndTo(mStartPoint, mEndPoint)

        val query = RouteSearch.DriveRouteQuery(
            fromAndTo, RouteSearch.DRIVING_SINGLE_DEFAULT, wayToPointList
            , null, ""
        )// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路

        routeSearch.setRouteSearchListener(object : RouteSearch.OnRouteSearchListener {
            override fun onDriveRouteSearched(result: DriveRouteResult?, errorCode: Int) {
                aMap.clear()// 清理地图上的所有覆盖物
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.paths != null) {
                        if (result.paths.size > 0) {
                            val drivePath = result.paths
                                .get(0) ?: return
                            val drivingRouteOverlay = DrivingRouteOverlay(
                                context, aMap, drivePath,
                                result.startPos,
                                result.targetPos, wayToPointList
                            )

                            drivingRouteOverlay.run {
                                setNodeIconVisibility(false)//设置节点marker是否显示
//                                setThroughPointIconVisibility(true)
                                setIsColorfulline(true)//是否用颜色展示交通拥堵情况，默认true
                                removeFromMap()
                                addToMap()  //addtomap 才会将途经点加入集合
                                zoomToSpan()
                            }
                            val dis = drivePath.distance.toInt()
                            val dur = drivePath.duration.toInt()
                            val des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")"
                        } else if (result.paths == null) {
                            ToastUtils.showShort(R.string.no_result)
                        }

                    } else {
                        ToastUtils.showShort(R.string.no_result)
                    }
                } else {
                    ToastUtils.showShort(errorCode.toString())
                }
            }

            override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {
            }

            override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) {
            }

            override fun onWalkRouteSearched(p0: WalkRouteResult?, p1: Int) {
            }
        })
        routeSearch.calculateDriveRouteAsyn(query)
    }
}