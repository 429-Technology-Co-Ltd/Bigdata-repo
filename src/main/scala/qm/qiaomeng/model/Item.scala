package qm.qiaomeng.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * @ClassName: Item
 * @Description: TODO
 * @Create by: LinYoung
 * @Date: 2021/1/22 14:32
 */
@JsonIgnoreProperties(ignoreUnknown = true)
case class Item(
                 platform: Int, //平台类型
                 item_id: String, //商品id
                 item_title: String, //商品标题
                 item_pic_list: Array[String], //商品图片列表
                 price: Double, //商品价格
                 end_price: Double, //折扣价格
                 volume_text: String, //销量
                 price_text: String, //价格信息
                 coupon_info: coupon_info, //优惠券信息
                 user_commission: Double, //返现金额
                 user_commission_text: String, //返现金额描述
                 detail_pic_list: Array[String], //详情图
                 shop_info: shop_info, //店铺信息
                 is_show_shop: Int,//是否展示店铺
                 is_collect: Int,//是否收藏
                 item_recommend_url: String,//推荐url
                 button_list: Array[button_info],//按钮列表
                 coupon_app_url: String,//优惠券app url
                 coupon_h5_url: String//优惠券h5 url
               )

case class coupon_info(
                        coupon_start_time: String, //优惠券开始时间
                        coupon_end_time: String, //优惠券结束时间
                        coupon_amount: Double, //优惠券金额
                        coupon_tips: String //优惠券描述
                      )

case class shop_info(
                      name: String, //店铺名称
                      icon_url: String, //店铺图标
                      link_url: String, //店铺链接
                      img_url: String, //店铺图片
                      item_desc: String, //商品描述
                      seller_service: String, //服务分
                      express_service: String //物流分
                    )

case class button_info(
                        button_name: String,//按钮名称
                        show_text: String,//
                        tips: String,//
                        h5_link: String,//
                        check_link: String//
                      )



