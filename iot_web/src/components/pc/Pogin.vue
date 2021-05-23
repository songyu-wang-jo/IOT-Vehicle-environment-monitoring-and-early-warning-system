<template>
  <div id="Pogin">
    <div id="title">
      欢迎登录汽车安全管理系统！
    </div>
    <div id="loginPanel">
      <el-form :model="userForm" status-icon :rules="userRules" ref="ruleForm2" label-width="80px" class="demo-ruleForm" style="margin-top: 60px">
        <el-form-item label="用户名" prop="username">
          <el-input type="text" v-model="userForm.username" auto-complete="off" style="width: 300px"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="userForm.password" auto-complete="off" style="width: 300px"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitUserForm('ruleForm2')">提交</el-button>
          <el-button @click="resetUserForm('ruleForm2')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {userLogin} from "@/api";

window.onresize = ()=>{
  var elementById = document.getElementById("title");
  elementById.style.fontSize = 0 +"px"
}
export default {
  name: "Pogin",
  data(){
    return{
      userForm: {
        username: '',
        password: ''
      },
      userRules: {
        username:[
          { required: true, message: '请输入用户名称', trigger: 'blur' },
          { min: 3, message: '最少3个字符', trigger: 'blur' }
        ],
        password:[
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 3, message: '最少3个字符', trigger: 'blur' }
        ]
      }
    }
  },
  methods:{
    submitUserForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          userLogin({username: this.userForm.username,password: this.userForm.password}).then(res=>{
            if(res.code === "2000"){
              this.$store.commit('setUserToken', 'admin');
              this.$message.success("登录成功！！！")
              this.$router.replace({path: '/'})
              this.$store.commit("setSessionId", this.userForm.username)
            }else {
              this.$message.error("登录异常！用户信息有误")
            }
          }).catch(err=>{
            this.$message.error("登录异常！服务器异常联系管理员")
          })
        }
      });
    },
    resetUserForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>

<style scoped>

#Pogin{
  width: 100vw;
  height: 100vh;
  background-color: #efefef;
}

#loginPanel{
  position: fixed;
  top: 200px;
  right: 300px;
  width: 400px;
  height: 300px;
  border: 1px saddlebrown solid;
  border-radius: 10px;
}

#title{
  position: fixed;
  left: 100px;
  top: 300px;
  font-size: 60px;
  color: #893d3d;
}

</style>
