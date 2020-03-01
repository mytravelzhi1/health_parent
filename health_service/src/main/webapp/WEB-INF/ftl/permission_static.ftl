<!DOCTYPE html>
<html>
<head>
    <!-- 页面meta -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>传智健康</title>
    <meta name="description" content="传智健康">
    <meta name="keywords" content="传智健康">
    <meta content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" name="viewport">
    <!-- 引入样式 -->
    <link rel="stylesheet" href="../plugins/elementui/index.css">
    <link rel="stylesheet" href="../plugins/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="../css/style.css">
</head>
<body class="hold-transition">
<div id="app">
    <div class="content-header">
        <h1>系统设置
            <small>权限管理</small>
        </h1>
        <el-breadcrumb separator-class="el-icon-arrow-right" class="breadcrumb">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>系统设置</el-breadcrumb-item>
            <el-breadcrumb-item>权限管理</el-breadcrumb-item>
        </el-breadcrumb>
    </div>
    <div class="app-container">
        <div class="box">
            <div class="filter-container">
                <!--<el-input placeholder="权限名称/权限关键词" v-model="pagination.queryString" style="width: 200px;"
                          class="filter-item"></el-input>
                <el-button @click="handleCurrentChange(1)" class="dalfBut">查询</el-button>-->
                <el-button type="primary" class="butT" @click="handleCreate()">新建</el-button>
            </div>
            <el-table size="small" current-row-key="id" :data="dataList" stripe highlight-current-row>
                <el-table-column type="index" align="center" label="序号"></el-table-column>
                <el-table-column prop="keyword" label="权限关键词" align="center"></el-table-column>
                <el-table-column prop="name" label="权限名称" align="center"></el-table-column>
                <el-table-column prop="description" label="权限描述" align="center"></el-table-column>
                <el-table-column label="操作" align="center">
                    <template slot-scope="scope">
                        <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
                        <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination-container">
                <el-pagination
                        class="pagiantion"
                        @current-change="handleCurrentChange"
                        :current-page="pagination.currentPage"
                        :page-size="pagination.pageSize"
                        layout="total, prev, pager, next, jumper"
                        :total="pagination.total">
                </el-pagination>
            </div>
            <!-- 新增标签弹层 -->
            <div class="add-form">
                <el-dialog title="新增权限" :visible.sync="dialogFormVisible">
                    <el-form ref="dataAddForm" :model="formData" :rules="rules" label-position="right"
                             label-width="100px">
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="权限关键词" prop="keyword">
                                    <el-input v-model="formData.keyword"/>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="权限名称" prop="name">
                                    <el-input v-model="formData.name"/>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        </el-row>
                        <el-row>
                            <el-col :span="24">
                                <el-form-item label="权限描述">
                                    <el-input v-model="formData.description" type="textarea"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogFormVisible = false">取消</el-button>
                        <el-button type="primary" @click="handleAdd()">确定</el-button>
                    </div>
                </el-dialog>
            </div>

            <!-- 编辑标签弹层 -->
            <div class="add-form">
                <el-dialog title="编辑权限" :visible.sync="dialogFormVisible4Edit">
                    <el-form ref="dataEditForm" :model="formData" :rules="rules" label-position="right"
                             label-width="100px">
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="权限关键词" prop="keyword">
                                    <el-input v-model="formData.keyword"/>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="权限名称" prop="name">
                                    <el-input v-model="formData.name"/>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="24">
                                <el-form-item label="权限描述">
                                    <el-input v-model="formData.description" type="textarea"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogFormVisible4Edit = false">取消</el-button>
                        <el-button type="primary" @click="handleEdit()">确定</el-button>
                    </div>
                </el-dialog>
            </div>
        </div>
    </div>
</div>
</body>
<!-- 引入组件库 -->
<script src="../js/vue.js"></script>
<script src="../plugins/elementui/index.js"></script>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<script src="../js/axios-0.18.0.js"></script>
<script>
    var vue = new Vue({
        el: '#app',
        data: {
            pagination: {//分页相关模型数据
                currentPage: 1,//当前页码
                pageSize: ${total},//每页显示的记录数
                total: ${total},//总记录数
                queryString: null//查询条件
            },
            dataList: [
                <#list dataList as permission>
                    {id:${permission.id!},
                        keyword: '${permission.keyword!}',
                        name: '${permission.name!}',
                        description: '${permission.description!}'},
                </#list>
            ],//当前页要展示的分页列表数据
            formData: {},//表单数据
            dialogFormVisible: false,//增加表单是否可见
            dialogFormVisible4Edit: false,//编辑表单是否可见
            rules: {//校验规则
                keyword: [{required: true, message: '权限关键词为必填项', trigger: 'blur'}],
                name: [{required: true, message: '权限名称为必填项', trigger: 'blur'}]
            }
        },
        //钩子函数，VUE对象初始化完成后自动执行
        created() {
            // vue对象初始化完成后自动查询分页数据
            this.findPage();
        },
        methods: {
            //编辑
            handleEdit() {
                //编辑窗口点击确定后,校验dataEditForm表单数据,校验成功发送ajax数据给后台
                this.$refs['dataEditForm'].validate((valid) => {
                    //校验成功
                    if (valid) {
                        axios.post("/permission/edit.do", this.formData).then((response) => {
                            //请求参数:formData.响应Result(flag,data,message)
                            if (response.data.flag) {
                                //隐藏新增窗口
                                this.dialogFormVisible4Edit = false;
                                this.$message({
                                    message: response.data.message,
                                    type: "success"
                                });
                            } else {
                                this.$message.error(response.data.message);
                            }
                        }).catch((error) => {

                        }).finally(() => {
                            //无论是否新增成功,均刷新列表数据
                            this.findPage();
                        });
                    } else {
                        //校验失败
                        this.$message.error("表单校验有误");
                    }
                });

            },
            //添加
            handleAdd() {
                //点击确定,表单提交,提交时做最后的校验,校验成功发送ajax请求将表单数据提交到后台
                //表单提交时做最后的校验
                this.$refs['dataAddForm'].validate((valid) => {
                    //校验成功
                    if (valid) {
                        axios.post("/permission/add.do", this.formData).then((response) => {
                            //请求参数:formData.响应Result(flag,data,message)
                            if (response.data.flag) {
                                //隐藏新增窗口
                                this.dialogFormVisible = false;
                                this.$message({
                                    message: response.data.message,
                                    type: "success"
                                });
                            } else {
                                this.$message.error(response.data.message);
                            }
                        }).catch((error) => {

                        }).finally(() => {
                            //无论是否新增成功,均刷新列表数据
                            this.findPage();
                        });
                    } else {
                        //校验失败
                        this.$message.error("表单校验有误");
                    }
                });
            },
            //分页查询
            findPage() {
                //分页查询,请求参数currentPage,pageSize,queryString(QueryPageBean),响应数据total,queryString(PageResult)
                //分页参数
                /* var param={
                     currentPage:this.pagination.currentPage,
                     pageSize:this.pagination.pageSize,
                     queryString:this.pagination.queryString
                 };
                 axios.post("/permission/findPage.do",param).then((response) => {
                     this.dataList=response.data.rows;
                     this.pagination.total=response.data.total;
                 }).catch((error) => {

                 }).finally(() => {

                 });*/

                //查询所有权限数据
               /* axios.get("/permission/findAll.do").then((response) => {
                    if (response.data.flag) {
                        this.dataList = response.data.data;
                        /!*this.$message({
                            message: response.data.message,
                            type: "success"
                        });*!/
                    } else {
                        this.$message.error(response.data.message);
                    }
                }).catch((error) => {

                }).finally(() => {

                })*/
            },
            // 重置表单
            resetForm() {
                //清空表格数据
                this.formData = {};
            },
            // 弹出添加窗口
            handleCreate() {
                //重置表单
                this.resetForm();
                //弹出添加窗口
                this.dialogFormVisible = true;

            },
            // 弹出编辑窗口
            handleUpdate(row) {
                //重置表单
                this.resetForm();
                //弹出编辑窗口
                this.dialogFormVisible4Edit = true;
                //回显当前权限数据
                axios.get("/permission/findById.do?id=" + row.id).then((response) => {
                    if (response.data.flag) {
                        this.formData = response.data.data;
                        /*  this.$message({
                              message:response.data.message,
                              type:"success"
                          });*/
                    } else {
                        this.$message.error(response.data.message);
                    }
                }).catch((error) => {

                }).finally(() => {

                })
            },
            //切换页码
            handleCurrentChange(currentPage) {
                this.pagination.currentPage = currentPage;
                this.findPage();
            },
            // 删除
            handleDelete(row) {
                this.$confirm('此操作将永久删除该权限, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    //点击确定,发送ajax请求到后台删除对应的检查项,请求参数,row.id,响应数据,无
                    axios.get("/permission/delete.do?id=" + row.id).then((response) => {
                        if (response.data.flag) {
                            //返回Result(flag,message,data)
                            this.$message({
                                message: response.data.message,
                                type: "success"
                            });
                        } else {
                            this.$message.error(response.data.message);
                        }
                    }).catch((error) => {
                        this.showMessage(error);
                    }).finally(() => {
                        //无论是否删除成功,均刷新列表
                        this.findPage();
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            showMessage(e) {
                if (e == 'Error: Request failed with status code 403') {
                    this.$message.error("权限不足");
                    return;
                } else {
                    this.$message.error("未知错误");
                    return;
                }
            }
        }
    })
</script>
</html>