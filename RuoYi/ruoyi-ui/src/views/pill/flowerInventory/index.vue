<template>
  <div class="app-container">
    <h3>库存管理</h3>
    <p style="margin-bottom: 10px; color: #666">
      显示所有花卉的库存情况。
    </p>
    <el-table v-loading="loading" :data="list" border>
      <el-table-column prop="flowerId" label="ID" width="80" />
      <el-table-column prop="flowerName" label="名称" />
      <el-table-column prop="flowerCode" label="编码" />
      <el-table-column prop="num" label="库存" width="100" />
      <el-table-column prop="warnValue" label="预警值" width="100" />
      <el-table-column label="预警" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.num < scope.row.warnValue" type="danger">
            低库存
          </el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { listFlower } from '@/api/pill/flower'

export default {
  name: 'FlowerInventory',
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
      listFlower({ pageNum: 1, pageSize: 9999 }).then(res => {
        this.list = res.rows || []
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


