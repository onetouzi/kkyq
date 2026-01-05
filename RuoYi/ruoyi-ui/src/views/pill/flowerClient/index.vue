<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true" :model="queryParams" size="small">
        <el-form-item label="名称">
          <el-input
            v-model="queryParams.flowerName"
            placeholder="花卉名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="queryParams.flowerType" placeholder="请选择类型" clearable>
            <el-option label="观花类" value="0" />
            <el-option label="观叶类" value="1" />
            <el-option label="观果类" value="2" />
            <el-option label="多肉类" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-row :gutter="20">
      <el-col
        v-for="item in list"
        :key="item.flowerId"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
      >
        <el-card class="flower-card" shadow="hover">
          <el-image
            v-if="item.image"
            :src="item.image"
            :preview-src-list="[item.image]"
            style="width: 100%; height: 180px"
            fit="cover"
          />
          <div class="info">
            <div class="name">{{ item.flowerName }}</div>
            <div class="price">￥{{ item.price }} / {{ item.unit || '件' }}</div>
            <div class="stock">库存：{{ item.num }}</div>
          </div>
          <div class="actions">
            <el-button type="primary" size="mini" @click="openBuyDialog(item)">
              购买
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

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

    <el-dialog title="购买花卉" :visible.sync="buyOpen" width="400px">
      <el-form :model="buyForm" label-width="90px">
        <el-form-item label="花卉">
          <span>{{ buyForm.flowerName }}</span>
        </el-form-item>
        <el-form-item label="单价">
          <span>￥{{ buyForm.price }}</span>
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="buyForm.quantity" :min="1" :max="buyForm.max" />
          <div style="font-size: 12px; color: #999; margin-top: 4px">
            当前库存：{{ buyForm.max }}
          </div>
        </el-form-item>
        <el-form-item label="总金额">
          <span style="color: #f56c6c; font-weight: bold">
            ￥{{ buyForm.price * buyForm.quantity }}
          </span>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="buyOpen = false">取 消</el-button>
        <el-button type="primary" @click="submitBuy">确认购买</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFlowerClient, buyFlowers } from '@/api/pill/flowerClient'

export default {
  name: 'FlowerClient',
  data() {
    return {
      loading: false,
      list: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        flowerName: undefined,
        flowerType: undefined
      },
      buyOpen: false,
      buyForm: {
        flowerId: undefined,
        flowerName: '',
        price: 0,
        quantity: 1,
        max: 0
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listFlowerClient(this.queryParams).then(res => {
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
        flowerName: undefined,
        flowerType: undefined
      }
      this.getList()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    openBuyDialog(item) {
      this.buyForm = {
        flowerId: item.flowerId,
        flowerName: item.flowerName,
        price: item.price,
        quantity: 1,
        max: item.num
      }
      this.buyOpen = true
    },
    submitBuy() {
      if (this.buyForm.quantity <= 0) {
        this.$message.warning('数量必须大于 0')
        return
      }
      if (this.buyForm.quantity > this.buyForm.max) {
        this.$message.warning('购买数量不能超过库存')
        return
      }
      const payload = [
        {
          flowerId: this.buyForm.flowerId,
          flowerName: this.buyForm.flowerName,
          quantity: this.buyForm.quantity,
          price: this.buyForm.price
        }
      ]
      buyFlowers(payload).then(() => {
        this.$message.success('购买成功')
        this.buyOpen = false
        this.getList()
      })
    }
  }
}
</script>

<style scoped>
.flower-card {
  margin-bottom: 20px;
}
.info {
  margin-top: 10px;
}
.name {
  font-weight: bold;
  margin-bottom: 4px;
}
.price {
  color: #f56c6c;
}
.stock {
  font-size: 12px;
  color: #999;
}
.actions {
  margin-top: 10px;
  text-align: right;
}
</style>


