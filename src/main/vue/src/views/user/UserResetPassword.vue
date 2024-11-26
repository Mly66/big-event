<script setup>
import { ref } from 'vue'
import useUserInfoStore from '@/stores/userInfo';
import { ElMessage } from 'element-plus';
import { userInfoPwdService } from '@/api/user';
import { useRouter } from 'vue-router';
const router = useRouter();
const userInfoStore = useUserInfoStore();
const userInfo = ref({ ...userInfoStore.info })

// 定义数据模型
const registerData = ref({
    old_pwd: '',
    new_pwd: '',
    re_pwd: ''
})

// 校验密码函数
const checkPassword = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请再次输入新密码'));
    } else if (value !== registerData.value.new_pwd) {
        callback(new Error('请确保两次密码一样'));
    } else {
        callback();
    }
}

// 定义表单校验规则
const rules = ref({
    old_pwd: [
        { required: true, message: '请输入初始密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度为5-16位非空字符', trigger: 'blur' }
    ],
    new_pwd: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度为5-16位非空字符', trigger: 'blur' }
    ],
    re_pwd: [
        { validator: checkPassword, trigger: 'blur' }
    ]
})

// 修改密码
const updateUserPwd = async () => {
    try {
        // 调用接口
        let result = await userInfoPwdService({
            old_pwd: registerData.value.old_pwd,
            new_pwd: registerData.value.new_pwd,
            re_pwd: registerData.value.re_pwd
        });
        if (result.success) {
            ElMessage.success(result.msg ? result.msg : '修改成功');
            // 修改 Pinia 中的个人信息
            userInfoStore.setInfo({ ...userInfoStore.info, password: registerData.value.new_pwd });
            router.push('/login');
        } else {
            ElMessage.success(result.msg ? result.msg : '修改成功');
            router.push('/login');
        }
    } catch (error) {
        ElMessage.error('修改失败，请重试');
    }
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>重置密码</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="registerData" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="初始密码" prop="old_pwd">
                        <el-input v-model="registerData.old_pwd" type="password"></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="new_pwd">
                        <el-input v-model="registerData.new_pwd" type="password"></el-input>
                    </el-form-item>
                    <el-form-item label="确认密码" prop="re_pwd">
                        <el-input v-model="registerData.re_pwd" type="password"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="updateUserPwd">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>
