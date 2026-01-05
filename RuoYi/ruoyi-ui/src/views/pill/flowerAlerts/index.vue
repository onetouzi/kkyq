<template>
  <div class="app-container">
    <h3>库存预警</h3>
    <p style="margin-bottom: 10px; color: #666">
      显示库存低于预警值的花卉列表。
    </p>
    <el-table v-loading="loading" :data="list" border>
      <el-table-column prop="flowerId" label="ID" width="80" />
      <el-table-column prop="flowerName" label="名称" />
      <el-table-column prop="flowerCode" label="编码" />
      <el-table-column prop="num" label="库存" width="100" />
      <el-table-column prop="warnValue" label="预警值" width="100" />
      <el-table-column label="状态" width="120">
        <template slot-scope="scope">
          <el-tag type="danger">低库存</el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { lowInventory } from '@/api/pill/flower'

export default {
  name: 'FlowerAlerts',
  data() {
    return {
      loading: false,
      list: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      lowInventory().then(res => {
        this.list = res.data || res.rows || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>
</style>


