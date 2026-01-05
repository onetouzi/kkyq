<template>
  <div class="app-container">
    <h3>数据报表</h3>
    <p style="margin-bottom: 10px; color: #666">
      展示花卉数量、订单、库存和销售统计。
    </p>

    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="6">
        <el-card>
          <div slot="header">花卉总数</div>
          <div class="stat-value">{{ statistics.totalFlowers || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div slot="header">今日订单数</div>
          <div class="stat-value">{{ statistics.todayOrders || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div slot="header">低库存数量</div>
          <div class="stat-value">{{ statistics.lowInventory || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div slot="header">总销售额(元)</div>
          <div class="stat-value">{{ statistics.totalSales || 0 }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card>
      <div slot="header">库存总量</div>
      <div class="stat-value">{{ statistics.totalInventory || 0 }}</div>
    </el-card>
  </div>
</template>

<script>
import { getStatistics } from '@/api/pill/flowerDashboard'

export default {
  name: 'FlowerReports',
  data() {
    return {
      loading: false,
      statistics: {}
    }
  },
  created() {
    this.fetchStatistics()
  },
  methods: {
    fetchStatistics() {
      this.loading = true
      getStatistics().then(res => {
        this.statistics = res.data || {}
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>
.stat-value {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  padding: 10px 0;
}
</style>


