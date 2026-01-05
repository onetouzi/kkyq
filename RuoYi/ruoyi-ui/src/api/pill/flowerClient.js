import request from '@/api/request'

// 查询可购买花卉列表
export function listFlowerClient(query) {
  return request({
    url: '/pill/flowerClient/list',
    method: 'get',
    params: query
  })
}

// 购买花卉
export function buyFlowers(data) {
  return request({
    url: '/pill/flowerClient/buy',
    method: 'post',
    data
  })
}


