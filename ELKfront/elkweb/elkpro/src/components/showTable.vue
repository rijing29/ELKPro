<template>
    <div>
        <el-container>
            <el-header>华为云报表导出</el-header>
            <el-main>
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
                <el-button  v-if="isSoftNameSelect" icon="el-icon-search" type="primary" @click="getSoftName" v-loading.fullscreen.lock="fullscreenLoading">软件效率</el-button>
                <el-button  v-if="isNodeTypeSelect" icon="el-icon-search" type="primary" @click="getNodeType" v-loading.fullscreen.lock="fullscreenLoading">节点效率</el-button>
                <el-table :data="tableData" stripe style="width: 100%">
                    <el-table-column prop="time" label="时间" width="180"></el-table-column>
                    <el-table-column prop="efficiency" label="效率"></el-table-column>
                </el-table>
            </el-main>
        </el-container>
    </div>

</template>

<script>
    export default {
        name:'showTable',
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
                tableData: [],
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
                end: ''
            }
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
            getNodeType(){
                this.fullscreenLoading = true;
                setTimeout(() => {
                    this.fullscreenLoading = false;
                }, 4000);
                console.log(this.startTime)
                console.log(this.stopTime)
                console.log(this.nodeType)
                console.log(this.nodeId)
                var url="/tableNodeType"
                var params={
                    'nodeType':this.nodeType,
                    'nodeId':this.nodeId,
                    'startTime':this.startTime,
                    'stopTime':this.stopTime
                }
                this.$http.get(url,{params}).then(res=>{
                    console.log(res)
                    this.tableData=res.data;
                })
            },
            getSoftName(){
                this.fullscreenLoading = true;
                setTimeout(() => {
                    this.fullscreenLoading = false;
                }, 4000);
                var url="/tableSoftName"
                var params={
                    'softName':this.softName,
                    'startTime':this.startTime,
                    'stopTime':this.stopTime
                }
                this.$http.get(url,{params}).then(res=>{
                    console.log(res)
                    this.tableData=res.data;
                })
            }
        }
    }
</script>
<style scoped>
    .el-container{
        height: 980px;
    }
    .el-header, .el-footer {
        background-color: #B3C0D1;
        color: #333;
        text-align: center;
        line-height: 60px;
    }
    .el-main {
        background-color: #E9EEF3;
        color: #333;
        /*text-align: center;*/
        height:100%;
    }
    .el-select{
        margin-right: 1%;
        width: 10%;
        line-height: 40px;
    }
    .el-table{
        width: 71%!important;
        margin: 0 auto;
        margin-top: 1%;
    }
</style>