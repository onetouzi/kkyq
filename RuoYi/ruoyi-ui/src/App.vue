<template>
  <div id="app">
    <el-container style="height: 100vh">
      <el-header>
        <div class="logo">花卉管理系统</div>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu
            class="el-menu-vertical-demo"
            :default-active="$route.path"
            router
          >
            <el-submenu index="/pill">
              <template slot="title">花卉管理</template>
              <el-menu-item index="/pill/flower">花卉信息</el-menu-item>
              <el-menu-item index="/pill/flowerClient">花卉商城</el-menu-item>
              <el-menu-item index="/pill/flowerOrder">我的订单</el-menu-item>
              <el-menu-item index="/pill/flowerAllOrder">全部订单</el-menu-item>
              <el-menu-item index="/pill/flowerInventory">库存管理</el-menu-item>
              <el-menu-item index="/pill/flowerAlerts">库存预警</el-menu-item>
              <el-menu-item index="/pill/flowerReports">数据报表</el-menu-item>
            </el-submenu>
          </el-menu>
        </el-aside>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style scoped>
.logo {
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  line-height: 60px;
}
</style>


