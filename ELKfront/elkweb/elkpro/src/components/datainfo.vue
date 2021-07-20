<template>
    <el-container>
      <el-header>华为云效率展示</el-header>
      <el-row class="grid-content bg-purple-dark">
        <el-col :span="24">
          <!--选择软件名/节点下拉框-->
          选择查询方式：
          <el-select v-model="selectMethod_value" placeholder="请选择" @change="SelectChange">
            <el-option v-for="item in selectMethod_options" :key="item.selectMethod_value" :label="item.label" :value="item.label"></el-option>
          </el-select>
          <!--软件名下拉框-->
          <span v-if="isSoftNameSelect">
            软件名：
          </span>
          <el-select v-model="software_value" placeholder="请选择" v-if="isSoftNameSelect">
            <el-option v-for="item in software_options" :key="item.software_value" :label="item.label" :value="item.label"></el-option>
          </el-select>
          <!--节点名下拉框-->
          <span v-if="isNodeTypeSelect">
            节点名：
          </span>
          <el-select v-model="node_value" placeholder="请选择" v-if="isNodeTypeSelect">
            <el-option v-for="item in node_options" :key="item.node_value"  :label="item.label" :value="item.label"></el-option>
          </el-select>
          <!--节点ID下拉框-->
          <span v-if="isNodeTypeSelect">
            节点ID：
          </span>
          <el-select v-model="nodeID_value" placeholder="请选择" v-if="isNodeTypeSelect">
            <el-option v-for="item in nodeID_options" :key="item.nodeID_value" :label="item.label" :value="item.label"></el-option>
          </el-select>
          <!--起始日期下拉框-->
          起始日期：
          <el-date-picker
                  v-model="begin"
                  type="date"
                  placeholder="选择起始日期"
                  format="yyyy/MM/dd HH:mm:ss"
                  value-format="yyyy/MM/dd HH:mm:ss"
                  style="margin-right: 1%; width: 10%;"
                  @change="selectStartTime"></el-date-picker>
          <!--终止日期下拉框-->
          终止日期：
          <el-date-picker
                  v-model="end"
                  type="date"
                  placeholder="选择终止日期"
                  format="yyyy/MM/dd HH:mm:ss"
                  value-format="yyyy/MM/dd HH:mm:ss"
                  style="margin-right: 1%; width: 10%;"
                  @change="selectStopTime"></el-date-picker>
          <!--按钮-->
          <el-button  v-if="isSoftNameSelect" icon="el-icon-search" type="primary" @click="getSoftNameEfficiency" v-loading.fullscreen.lock="fullscreenLoading">软件效率</el-button>
          <el-button  v-if="isNodeTypeSelect" icon="el-icon-search" type="primary" @click="getNodeTypeEfficiency" v-loading.fullscreen.lock="fullscreenLoading">节点效率</el-button>
        </el-col>
      </el-row>
      <el-main>
        <!--数据展示图-->
        <v-chart class="echarts" :option="option" />
        <span>平均效率：{{avegEffiency[0]}}</span>
      </el-main>
    </el-container>
</template>

<script>
//  echarts引入
  import * as echarts from 'echarts/core';
  import {
    TitleComponent,
    TooltipComponent,
    GridComponent,
    LegendComponent
  } from 'echarts/components';
  import {
    BarChart
  } from 'echarts/charts';
  import {
    CanvasRenderer
  } from 'echarts/renderers';

  echarts.use(
      [TitleComponent, TooltipComponent, GridComponent, LegendComponent, BarChart, CanvasRenderer]
  );
  import VChart, { THEME_KEY } from "vue-echarts";
  //开始渲染
  export default {
    name: 'datainfo',
    components: {
      VChart
    },
    watch: {
      software_value: function(newV, oldV) {
          this.softName=newV
      },
      node_value: function(newV, oldV) {
          this.nodeType=newV
      },
      nodeID_value: function(newV, oldV) {
          this.nodeId=newV
      }
    },
    data() {
      return {
        fullscreenLoading: false,
        softName:'',
        startTime:'',
        stopTime:'',
        nodeType:'',
        nodeId:'',
        isSoftNameSelect:false,
        isNodeTypeSelect:false,
        avegEffiency:[0],
        tableData: [],
        selectMethod_options:[
            {selectMethod_value:'选项1',label:'按软件名查询'},
            {selectMethod_value:'选项2',label:'按节点查询'}
        ],
        selectMethod_value: '',
        //软件名下拉框值
        software_options: [
          {software_value: '选项1',label: 'GEOEASTDL'},
          {software_value: '选项2',label: 'Geoeast'},
          {software_value: '选项3',label: 'PWIN'},
          {software_value: '选项4',label: 'TOMODEL'},
          {software_value: '选项5',label: 'PARADIGM'},
          {software_value: '选项6',label: 'PARADIGMDL'},
          {software_value: '选项7',label: 'ZHIKONG'},
          {software_value: '选项8',label: 'PSTM'}
        ],
        software_value: '',
        //节点名下拉框值
        node_options: [
          {node_value: '选项1',label: 'hwnode'},
          {node_value: '选项2',label: 'hwgnode'}
        ],
        node_value: '',
        //节点ID下拉框值
        nodeID_options: [
          {nodeID_value: '选项1',label: '1'},
          {nodeID_value: '选项2',label: '2'},
          {nodeID_value: '选项3',label: '3'}
        ],
        nodeID_value: '',
        //起始日期
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
        //终止日期
        disabledDate2(time) {
          return time.getTime() > Date.now();
        },
        begin: '',
        end: '',
        option: {
          title: {text: '节点利用率',subtext: '数据来自数据库统计'},
          tooltip: {trigger: 'axis',axisPointer: {type: 'shadow'}},
          legend: {data: ['2011年', '2012年']},
          grid: {left: '3%',right: '4%',bottom: '3%',containLabel: true},
          xAxis: { type: 'value',boundaryGap: [0, 0.01]},
          yAxis: {
            type: 'category',
            data: ['1', '2','3','4','5','6']
          },
          series: [
            {
              name: '效率展示',
              type: 'bar',
              data: [1,2,3,4,5,6]
            },
          ]
        }
      }
    },
    created(){
        console.log(this.option.series[0].data)
        console.log(this.option.yAxis.data)
    },
    methods:{
        SelectChange(val){
            console.log(val)
            if(val=='按软件名查询') {
                this.isSoftNameSelect = true;
                this.isNodeTypeSelect=false;
            }
            else {
                this.isNodeTypeSelect = true;
                this.isSoftNameSelect=false;
            }
        },
        selectStartTime(val){
            this.startTime = val;
        },
        selectStopTime(val){
            this.stopTime = val;
        },
        //  根据软件名查效率
        getSoftNameEfficiency(){
            this.fullscreenLoading = true;
            setTimeout(() => {
                this.fullscreenLoading = false;
            }, 4000);
            var url="/calSoftName"
            var params={
                'softName':this.softName,
                'startTime':this.startTime,
                'stopTime':this.stopTime
            }
            this.$http.get(url,{params}).then(res=>{
                //设置横轴
                this.option.series[0].data=res.data.value
                //设置纵轴
                this.option.yAxis.data=res.data.key
                this.avegEffiency=res.data.ave
                console.log(this.avegEffiency[0])
            })
        },
        //  根据节点名查效率
        getNodeTypeEfficiency(){
            this.fullscreenLoading = true;
            setTimeout(() => {
                this.fullscreenLoading = false;
            }, 4000);
            console.log(this.startTime)
            console.log(this.stopTime)
            console.log(this.nodeType)
            console.log(this.nodeId)
            var url="/calNodeType"
            var params={
                'nodeType':this.nodeType,
                'nodeId':this.nodeId,
                'startTime':this.startTime,
                'stopTime':this.stopTime
            }
            this.$http.get(url,{params}).then(res=>{
                //设置横轴
                this.option.series[0].data=res.data.value
                //设置纵轴
                this.option.yAxis.data=res.data.key
                this.avegEffiency=res.data.ave
                console.log(this.avegEffiency[0])
            })
        }
    },
  }
</script>

<style scoped>
  .el-header, .el-footer {
    background-color: #B3C0D1;
    color: #ffffff;
    text-align: center;
    line-height: 60px;
    font-size: large;
    font-weight: bold;
  }
  .el-main {
    background-color: #fff;
    color: #333;
    text-align: center;
    line-height: 160px;
    height:82vh;
    padding: 0;
  }
  .el-select{
    margin-right: 1%;
    width: 10%;
    line-height: 10px;
  }

  .bg-purple-dark {
    background: #E9EEF3;
  }
  .grid-content {
    padding-top: 20px;
    padding-bottom: 30px;
    min-height: 36px;
  }
  .el-row{
    background-color: #ffffff;
    padding: 20px;
  }
  .el-container{
    display: flex;
    flex-wrap: nowrap;
  }
</style>