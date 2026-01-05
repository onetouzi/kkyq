<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true" :model="queryParams" size="small">
        <el-form-item label="用户">
          <el-input
            v-model="queryParams.userName"
            placeholder="用户名"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="花卉">
          <el-input
            v-model="queryParams.flowerName"
            placeholder="花卉名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="已支付" value="PAID" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table v-loading="loading" :data="list" border>
      <el-table-column prop="orderId" label="订单ID" width="100" />
      <el-table-column prop="userName" label="用户名" />
      <el-table-column prop="flowerName" label="花卉名称" />
      <el-table-column prop="flowerCode" label="花卉编码" />
      <el-table-column prop="quantity" label="数量" width="80" />
      <el-table-column prop="totalPrice" label="总金额(元)" width="120" />
      <el-table-column prop="status" label="状态" width="110">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 'PAID'" type="warning">已支付</el-tag>
          <el-tag v-else-if="scope.row.status === 'COMPLETED'" type="success">已完成</el-tag>
          <el-tag v-else type="info">已取消</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="180" />
    </el-table>

    <el-pagination
      v-show="total > 0"
      style="margin-top: 10px; text-align: right"
      background
      layout="total, prev, pager, next"
      :total="total"
      :page-size="queryParams.pageSize"
      :current-page="queryParams.pageNum"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import { listAllOrder } from '@/api/pill/flowerAllOrder'

export default {
  name: 'FlowerAllOrder',
  data() {
    return {
      loading: false,
      list: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        flowerName: undefined,
        status: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listAllOrder(this.queryParams).then(res => {
        this.list = res.rows || []
        this.total = res.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        flowerName: undefined,
        status: undefined
      }
      this.getList()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    }
  }
}
</script>

<style scoped>
</style>


