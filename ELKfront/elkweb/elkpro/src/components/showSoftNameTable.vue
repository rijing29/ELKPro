<template>
    <div>
        <el-container>
            <el-header>华为云报表导出</el-header>
            <el-main>
                请选择一个年份：
                <el-select v-model="yearvalue" placeholder="请选择">
                    <el-option v-for="item in year_options" :key="item.yearvalue" :label="item.label" :value="item.label"></el-option>
                </el-select>年
                请选择一个月份：
                <el-select v-model="monthvalue" placeholder="请选择">
                    <el-option v-for="item in month_options" :key="item.monthvalue" :label="item.label" :value="item.label"></el-option>
                </el-select>月
                <el-button icon="el-icon-search" type="primary" @click="searchSoftNameEfficiency" v-loading.fullscreen.lock="fullscreenLoading">查询</el-button>
                <el-button icon="el-icon-search" type="primary" @click="export2Excel">导出excel</el-button>
                <el-table :data="tableData" stripe style="width: 100%">
                    <el-table-column prop="softName" label="软件名" width="360"></el-table-column>
                    <el-table-column prop="time" label="时间" width="360"></el-table-column>
                    <el-table-column prop="efficiency" label="效率"></el-table-column>
                </el-table>
            </el-main>
        </el-container>
    </div>

</template>

<script>
    export default {
        watch: {
            yearvalue: function(newV, oldV) {
                this.year=newV
            },
            monthvalue: function(newV, oldV) {
                this.month=newV
            }
        },
        data() {
            return {
                fullscreenLoading: false,
                year:'',
                month:'',
                tableData: [],
                month_options: [{monthvalue: '选项1', label: '1'},
                    {monthvalue: '选项2', label: '2'},
                    {monthvalue: '选项3', label: '3'},
                    {monthvalue: '选项4', label: '4'},
                    {monthvalue: '选项5', label: '5'},
                    {monthvalue: '选项6', label: '6'},
                    {monthvalue: '选项7', label: '7'},
                    {monthvalue: '选项8', label: '8'},
                    {monthvalue: '选项9', label: '9'},
                    {monthvalue: '选项10', label: '10'},
                    {monthvalue: '选项11', label: '11'},
                    {monthvalue: '选项12', label: '12'}],
                monthvalue: '',
                year_options:[{yearvalue: '选项1', label: '2017'},
                    {yearvalue: '选项2', label: '2018'},
                    {yearvalue: '选项3', label: '2019'},
                    {yearvalue: '选项4', label: '2020'},
                    {yearvalue: '选项5', label: '2021'},],
                yearvalue:''
            }
        },
        created(){
            // this.getSoftName();
        },
        methods:{
          getSoftName(){
              var url="/searchSoftName"
              this.$http.get(url).then(res=>{
                  this.tableData=res.data
                  console.log(res)
              })
          },
          searchSoftNameEfficiency(){
              console.log(this.year)
              console.log(this.month)
              this.fullscreenLoading = true;
              setTimeout(() => {
                  this.fullscreenLoading = false;
              }, 500);
              var url="/searchSoftNameEfficiency"
              var params={
                  'year': this.year,
                  'month': this.month,
              }
              this.$http.get(url,{params}).then(res=>{
                  this.tableData=res.data
                  console.log(this.tableData)
              })
          },
            //导出excel的信息集合
            export2Excel() {
                require.ensure([], () => {
                    const { export_json_to_excel } = require("../tools/Export2Excel.js");
                    // 设置自己的excel表头
                    const tHeader = [
                        "软件名",
                        "时间",
                        "月平均效率",
                    ]; // 上面设置Excel的表格第一行的标题
                    const filterVal = [
                        "softName",
                        "time",
                        "efficiency",
                    ]; // client_id client_name client_phone 为tableData的属性
                    const list = this.tableData; //把data里的tableData存到list
                    const data = this.formatJson(filterVal, list);
                    export_json_to_excel(tHeader, data, "软件名效率excel");
                });
            },
            formatJson(filterVal, jsonData) {
                return jsonData.map(v => filterVal.map(j => v[j]));
            }

        },
    }
</script>
<style scoped>
    .el-container{height: 980px;}
    .el-header, .el-footer {background-color: #B3C0D1;color: #333;text-align: center;line-height: 60px;}
    .el-main {background-color: #E9EEF3;color: #333;height:100%;}
    .el-select{margin-right: 1%;width: 10%;line-height: 40px;}
    .el-table{width: 71%!important;margin: 0 auto;margin-top: 1%;}
</style>