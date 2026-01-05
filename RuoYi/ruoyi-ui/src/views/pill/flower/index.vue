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
        <el-form-item label="编码">
          <el-input
            v-model="queryParams.flowerCode"
            placeholder="花卉编码"
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

    <div class="table-operations">
      <el-button type="primary" icon="el-icon-plus" size="small" @click="handleAdd">
        新增
      </el-button>
      <el-button
        type="danger"
        icon="el-icon-delete"
        size="small"
        :disabled="multiple"
        @click="handleDelete"
      >
        删除
      </el-button>
      <el-button
        type="warning"
        icon="el-icon-download"
        size="small"
        @click="handleExport"
      >
        导出
      </el-button>
    </div>

    <el-table
      v-loading="loading"
      :data="flowerList"
      border
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="flowerId" label="ID" width="80" />
      <el-table-column prop="image" label="图片" width="100">
        <template slot-scope="scope">
          <el-image
            v-if="scope.row.image"
            :src="scope.row.image"
            :preview-src-list="[scope.row.image]"
            style="width: 60px; height: 60px"
            fit="cover"
          />
        </template>
      </el-table-column>
      <el-table-column prop="flowerName" label="名称" min-width="120" />
      <el-table-column prop="flowerCode" label="编码" min-width="100" />
      <el-table-column prop="flowerType" label="类型" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.flowerType === '0'" type="primary">观花类</el-tag>
          <el-tag v-else-if="scope.row.flowerType === '1'" type="success">观叶类</el-tag>
          <el-tag v-else-if="scope.row.flowerType === '2'" type="warning">观果类</el-tag>
          <el-tag v-else type="info">多肉类</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="color" label="颜色" width="90" />
      <el-table-column prop="floweringPeriod" label="花期" width="120" />
      <el-table-column prop="price" label="价格(元)" width="100" />
      <el-table-column prop="num" label="库存" width="90" />
      <el-table-column prop="warnValue" label="预警值" width="90" />
      <el-table-column prop="unit" label="单位" width="80" />
      <el-table-column prop="status" label="状态" width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'info'">
            {{ scope.row.status === '0' ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="160">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="handleUpdate(scope.row)">
            编辑
          </el-button>
          <el-button type="text" size="mini" @click="handleDelete(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-show="total > 0"
      style="margin-top: 10px; text-align: right"
      background
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      :page-size="queryParams.pageSize"
      :current-page="queryParams.pageNum"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <el-dialog :title="dialogTitle" :visible.sync="open" width="600px">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="名称" prop="flowerName">
          <el-input v-model="form.flowerName" />
        </el-form-item>
        <el-form-item label="编码" prop="flowerCode">
          <el-input v-model="form.flowerCode" />
        </el-form-item>
        <el-form-item label="类型" prop="flowerType">
          <el-select v-model="form.flowerType" placeholder="请选择类型">
            <el-option label="观花类" value="0" />
            <el-option label="观叶类" value="1" />
            <el-option label="观果类" value="2" />
            <el-option label="多肉类" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="颜色" prop="color">
          <el-input v-model="form.color" />
        </el-form-item>
        <el-form-item label="花期" prop="floweringPeriod">
          <el-input v-model="form.floweringPeriod" />
        </el-form-item>
        <el-form-item label="价格(元)" prop="price">
          <el-input-number v-model="form.price" :min="0" />
        </el-form-item>
        <el-form-item label="库存" prop="num">
          <el-input-number v-model="form.num" :min="0" />
        </el-form-item>
        <el-form-item label="预警值" prop="warnValue">
          <el-input-number v-model="form.warnValue" :min="0" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="图片地址" prop="image">
          <el-input v-model="form.image" placeholder="可直接填后端返回的图片路径" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listFlower,
  getFlower,
  addFlower,
  updateFlower,
  delFlower,
  exportFlower
} from '@/api/pill/flower'

export default {
  name: 'Flower',
  data() {
    return {
      loading: false,
      flowerList: [],
      total: 0,
      multiple: true,
      ids: [],
      open: false,
      dialogTitle: '',
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        flowerName: undefined,
        flowerCode: undefined,
        flowerType: undefined
      },
      form: {
        flowerId: undefined,
        flowerName: '',
        flowerCode: '',
        flowerType: '0',
        color: '',
        floweringPeriod: '',
        price: 0,
        num: 0,
        warnValue: 0,
        unit: '',
        image: '',
        status: '0'
      },
      rules: {
        flowerName: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        flowerCode: [{ required: true, message: '请输入编码', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listFlower(this.queryParams).then(res => {
        this.flowerList = res.rows || []
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
        flowerCode: undefined,
        flowerType: undefined
      }
      this.getList()
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.flowerId)
      this.multiple = !selection.length
    },
    resetForm() {
      this.form = {
        flowerId: undefined,
        flowerName: '',
        flowerCode: '',
        flowerType: '0',
        color: '',
        floweringPeriod: '',
        price: 0,
        num: 0,
        warnValue: 0,
        unit: '',
        image: '',
        status: '0'
      }
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    },
    handleAdd() {
      this.resetForm()
      this.dialogTitle = '新增花卉'
      this.open = true
    },
    handleUpdate(row) {
      this.resetForm()
      getFlower(row.flowerId).then(res => {
        this.form = Object.assign({}, res.data || {})
        this.dialogTitle = '编辑花卉'
        this.open = true
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const api = this.form.flowerId ? updateFlower : addFlower
        api(this.form).then(() => {
          this.$message.success('操作成功')
          this.open = false
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const ids = row && row.flowerId ? row.flowerId : this.ids.join(',')
      if (!ids) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm('确认删除选中的花卉吗？', '提示', {
        type: 'warning'
      }).then(() => {
        delFlower(ids).then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
      }).catch(() => {})
    },
    handleExport() {
      exportFlower(this.queryParams).then(blobRes => {
        const blob = new Blob([blobRes])
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = '花卉信息.xlsx'
        a.click()
        window.URL.revokeObjectURL(url)
      })
    }
  }
}
</script>

<style scoped>
</style>


