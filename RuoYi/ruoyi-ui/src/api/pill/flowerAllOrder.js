import request from '@/api/request'

// 查询全部订单
export function listAllOrder(query) {
  return request({
    url: '/pill/flowerAllOrder/list',
    method: 'get',
    params: query
  })
}


