<template>
    <div class="wrapper">
        <div style="margin: 200px auto; background-color: #fff; width: 350px; height: 350px; padding: 20px; border-radius: 10px">
            <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登录</b></div>
            <el-form :rules="rules" :model="user" ref="loginForm">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="user.username" size="medium" style="margin: 10px 0" prefix-icon="el-icon-user"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="user.password" size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password></el-input>
                </el-form-item>
                <el-form-item>
                    <el-radio v-model="type" label="systemAdmin">系统管理员</el-radio>
                    <el-radio style="position: relative;left: 80px" v-model="type" label="dormitoryAdmin">宿舍管理员</el-radio>
                </el-form-item>
                <div>
                    <el-button type="primary" @click="handelSubmit" :loading="logining">登录</el-button>
<!--                    <el-button type="warning" @click="$router.push('/regist')">注册</el-button>-->
                </div>
            </el-form>
        </div>
    </div>
</template>

<script>

export default {
    name: "Login",
    data() {
        return {
            logining: false,
            user: {},
            type: 'systemAdmin',
            rules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
                ]
            }
        }
    },
    methods: {
        handelSubmit() {
            this.$refs["loginForm"].validate((valid) => {
                if (valid) {
                    this.logining = true
                    let _this = this
                    if(_this.type == 'systemAdmin'){
                        axios.get('localhost:8090/systemAdmin/login').then()
                    }

                } else {
                    return false;
                }
            });
        }
    }
}

</script>

<style scoped>
.wrapper {
    height: 100vh;
    background-image: linear-gradient(to top right, #FC466B, #3F5EFB);
    overflow: hidden;
}
</style>