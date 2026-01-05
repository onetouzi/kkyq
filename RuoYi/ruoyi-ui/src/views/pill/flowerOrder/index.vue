<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true" :model="queryParams" size="small">
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
      <el-table-column prop="flowerName" label="花卉名称" />
      <el-table-column prop="flowerCode" label="花卉编码" />
      <el-table-column prop="quantity" label="数量" width="80" />
      <el-table-column prop="unitPrice" label="单价(元)" width="100" />
      <el-table-column prop="totalPrice" label="总金额(元)" width="120" />
      <el-table-column prop="status" label="状态" width="110">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 'PAID'" type="warning">已支付</el-tag>
          <el-tag v-else-if="scope.row.status === 'COMPLETED'" type="success">已完成</el-tag>
          <el-tag v-else type="info">已取消</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="180" />
      <el-table-column label="操作" fixed="right" width="200">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="viewDetail(scope.row)">
            详情
          </el-button>
          <el-button
            v-if="scope.row.status === 'PAID'"
            type="text"
            size="mini"
            @click="handleCancel(scope.row)"
          >
            取消
          </el-button>
          <el-button
            v-if="scope.row.status === 'PAID'"
            type="text"
            size="mini"
            @click="handleComplete(scope.row)"
          >
            完成
          </el-button>
        </template>
      </el-table-column>
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

    <el-dialog title="订单详情" :visible.sync="detailOpen" width="500px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="订单ID">{{ detail.orderId }}</el-descriptions-item>
        <el-descriptions-item label="花卉名称">{{ detail.flowerName }}</el-descriptions-item>
        <el-descriptions-item label="花卉编码">{{ detail.flowerCode }}</el-descriptions-item>
        <el-descriptions-item label="数量">{{ detail.quantity }}</el-descriptions-item>
        <el-descriptions-item label="单价">{{ detail.unitPrice }}</el-descriptions-item>
        <el-descriptions-item label="总金额">{{ detail.totalPrice }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ statusLabel(detail.status) }}</el-descriptions-item>
        <el-descriptions-item label="下单时间">{{ detail.createTime }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listMyOrder,
  getOrder,
  cancelOrder,
  completeOrder
} from '@/api/pill/flowerOrder'

export default {
  name: 'FlowerOrder',
  data() {
    return {
      loading: false,
      list: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: undefined
      },
      detailOpen: false,
      detail: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listMyOrder(this.queryParams).then(res => {
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
        status: undefined
      }
      this.getList()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    statusLabel(status) {
      if (status === 'PAID') return '已支付'
      if (status === 'COMPLETED') return '已完成'
      if (status === 'CANCELLED') return '已取消'
      return status || '-'
    },
    viewDetail(row) {
      getOrder(row.orderId).then(res => {
        this.detail = res.data || {}
        this.detailOpen = true
      })
    },
    handleCancel(row) {
      this.$confirm('确认取消该订单吗？', '提示', { type: 'warning' })
        .then(() => cancelOrder(row.orderId))
        .then(() => {
          this.$message.success('取消成功')
          this.getList()
        })
        .catch(() => {})
    },
    handleComplete(row) {
      this.$confirm('确认将该订单标记为已完成吗？', '提示', { type: 'info' })
        .then(() => completeOrder(row.orderId))
        .then(() => {
          this.$message.success('操作成功')
          this.getList()
        })
        .catch(() => {})
    }
  }
}
</script>

<style scoped>
</style>


