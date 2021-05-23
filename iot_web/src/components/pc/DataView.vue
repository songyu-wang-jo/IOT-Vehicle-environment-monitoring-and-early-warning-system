<template>
  <div id="messageView">
    <div id="top">
      <el-main v-loading="isTableLoading" id="tempHumiContent" class="content"></el-main>
    </div>
    <div id="bottom">
      <el-container id="errorTable">
        <el-main>
          <el-table
              :data="warnData"
              border
              v-loading="isTableLoading"
              style="width: 100%">
            <el-table-column
                prop="warnType"
                label="异常类型"
                width="180">
            </el-table-column>
            <el-table-column
                prop="warnDesc"
                label="异常描述"
                width="180">
            </el-table-column>
            <el-table-column
                prop="datetime"
                label="记录日期">
            </el-table-column>
          </el-table>
        </el-main>
        <el-footer style="text-align: center">
          <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="currentPage"
              :page-sizes="[5,2]"
              :page-size="warnDataPageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="warnDataTotal">
          </el-pagination>
        </el-footer>
    </el-container>
    </div>
  </div>
</template>

<script>
import {deviceHeartBeet, deviceWarn} from "@/api";

export default {
  name: "MessageView",
  data(){
    return{
      timeData: [],
      tempData: [],
      humiData: [],
      warnData: [],
      currentPage: 1,
      warnDataTotal: 0,
      warnDataPageSize: 5,
      isTableLoading: false,
      isTempLoading: false,
    }
  },
  methods:{
    handleSizeChange(val) {
      this.warnDataPageSize = val
      this.initDeviceWarnData(this.warnDataPageSize,this.currentPage)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.initDeviceWarnData(this.warnDataPageSize,this.currentPage)
    },
    initDeviceHeartBeetData() {
      deviceHeartBeet({size: 100000, current: 1}).then(res=>{
        if (res.code==="2000"){
          res.data.records.forEach((item,index)=>{
            this.timeData.push(item.datetime)
            this.tempData.push(item.temp)
            this.humiData.push(item.humi/100)
            let tempECharts = this.$echarts.init(document.getElementById('tempHumiContent'));
            tempECharts.setOption({
              title: {
                text: '设备心跳温度湿度数据展示',
                subtext: '数据来自Mysql数据库',
                left: 'center'
              },
              tooltip: {
                trigger: 'axis',
                axisPointer: {
                  animation: false
                }
              },
              legend: {
                data: ['温度', '湿度'],
                left: 10
              },
              toolbox: {
                feature: {
                  dataZoom: {
                    yAxisIndex: 'none'
                  },
                  restore: {},
                  saveAsImage: {}
                }
              },
              axisPointer: {
                link: {xAxisIndex: 'all'}
              },
              dataZoom: [
                {
                  show: true,
                  realtime: true,
                  start: 30,
                  end: 70,
                  xAxisIndex: [0, 1]
                },
                {
                  type: 'inside',
                  realtime: true,
                  start: 30,
                  end: 70,
                  xAxisIndex: [0, 1]
                }
              ],
              grid: [{
                left: 50,
                right: 50,
                height: '35%'
              }, {
                left: 50,
                right: 50,
                top: '55%',
                height: '35%'
              }],
              xAxis: [
                {
                  type: 'category',
                  boundaryGap: false,
                  axisLine: {onZero: true},
                  data: this.timeData
                },
                {
                  gridIndex: 1,
                  type: 'category',
                  boundaryGap: false,
                  axisLine: {onZero: true},
                  data: this.timeData,
                  position: 'top'
                }
              ],
              yAxis: [
                {
                  name: '温度(摄氏度)',
                  type: 'value',
                  max: 100
                },
                {
                  gridIndex: 1,
                  name: '湿度(占比)',
                  type: 'value',
                  inverse: true
                }
              ],
              series: [
                {
                  name: '温度',
                  type: 'line',
                  symbolSize: 8,
                  hoverAnimation: false,
                  data: this.tempData
                },
                {
                  name: '湿度',
                  type: 'line',
                  xAxisIndex: 1,
                  yAxisIndex: 1,
                  symbolSize: 8,
                  hoverAnimation: false,
                  data: this.humiData
                }
              ]
            })
          })
        }else {
          this.$message.error("获取数据异常！请检查参数")
        }
      }).catch(err=>{
        this.$message.error("服务器异常，请联系管理员！！！")
      })
    },
    initDeviceWarnData(size, current){
      this.isTableLoading=true;
      deviceWarn({size: size, current: current}).then(res=>{
        this.warnData = res.data.records;
        this.warnDataTotal = res.data.total;
        this.isTableLoading=false;
      }).catch(err=>{
        this.$message.error("服务器异常，请联系管理员！！！")
      })
    }
  },
  mounted() {
  },
  created() {
    this.initDeviceHeartBeetData();
    this.initDeviceWarnData(5,1);
  }
}
</script>

<style scoped>
#top {

}

#tempHumiContent{
  height: 400px;
}

.content{
  height: 350px;
}

#errorTable{
  height: 400px;
}

#bottom {

}
</style>
