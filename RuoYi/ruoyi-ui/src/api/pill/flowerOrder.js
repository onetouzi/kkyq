import request from '@/api/request'

// 查询我的订单
export function listMyOrder(query) {
  return request({
    url: '/pill/flowerOrder/list',
    method: 'get',
    params: query
  })
}

// 查询订单详情
export function getOrder(orderId) {
  return request({
    url: `/pill/flowerOrder/${orderId}`,
    method: 'get'
  })
}

// 取消订单
export function cancelOrder(orderId) {
  return request({
    url: `/pill/flowerOrder/cancel/${orderId}`,
    method: 'put'
  })
}

// 完成订单
export function completeOrder(orderId) {
  return request({
    url: `/pill/flowerOrder/complete/${orderId}`,
    method: 'put'
  })
}


