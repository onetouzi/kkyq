import request from '@/api/request'

// 查询花卉列表
export function listFlower(query) {
  return request({
    url: '/pill/flower/list',
    method: 'get',
    params: query
  })
}

// 查询花卉详情
export function getFlower(flowerId) {
  return request({
    url: `/pill/flower/${flowerId}`,
    method: 'get'
  })
}

// 新增花卉
export function addFlower(data) {
  return request({
    url: '/pill/flower',
    method: 'post',
    data
  })
}

// 修改花卉
export function updateFlower(data) {
  return request({
    url: '/pill/flower',
    method: 'put',
    data
  })
}

// 删除花卉
export function delFlower(flowerIds) {
  return request({
    url: `/pill/flower/${flowerIds}`,
    method: 'delete'
  })
}

// 导出花卉
export function exportFlower(query) {
  return request({
    url: '/pill/flower/export',
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}

// 低库存花卉
export function lowInventory() {
  return request({
    url: '/pill/flower/lowInventory',
    method: 'get'
  })
}


