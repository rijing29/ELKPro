<template>
    <div>
        <el-container>
            <el-header>华为云报表导出</el-header>
            <el-main>
                请选择一个月份：<el-select v-model="value" placeholder="请选择">
                    <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
                </el-select>月
                <el-table :data="tableData" stripe style="width: 100%">
                    <el-table-column prop="date" label="日期" width="180"></el-table-column>
                    <el-table-column prop="name" label="姓名" width="180"></el-table-column>
                    <el-table-column prop="address" label="地址"></el-table-column>
                </el-table>
            </el-main>
        </el-container>
    </div>

</template>

<script>
    export default {
        data() {
            return {
                tableData: [{
                    date: '2016-05-02',
                    name: '王小虎',
                    address: '上海市普陀区金沙江路 1518 弄'
                }, {
                    date: '2016-05-04',
                    name: '王小虎',
                    address: '上海市普陀区金沙江路 1517 弄'
                }, {
                    date: '2016-05-01',
                    name: '王小虎',
                    address: '上海市普陀区金沙江路 1519 弄'
                }, {
                    date: '2016-05-03',
                    name: '王小虎',
                    address: '上海市普陀区金沙江路 1516 弄'
                }],
                options: [{value: '选项1', label: '1'},
                    {value: '选项2', label: '2'},
                    {value: '选项3', label: '3'},
                    {value: '选项4', label: '4'},
                    {value: '选项5', label: '5'},
                    {value: '选项6', label: '6'},
                    {value: '选项7', label: '7'},
                    {value: '选项8', label: '8'},
                    {value: '选项9', label: '9'},
                    {value: '选项10', label: '10'},
                    {value: '选项11', label: '11'},
                    {value: '选项12', label: '12'}],
                value: ''

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