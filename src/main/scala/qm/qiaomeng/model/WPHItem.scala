package qm.qiaomeng.model

/**
 * @ClassName: WPHitem
 * @Description: TODO
 * @Create by: LinYoung
 * @Date: 2021/2/13 17:39
 */
case class WPHItem(
                    source: String,
                    goodsId: Long,
                    goodsName: String,
                    spuId: String,
                    schemeStartTime: Long,
                    schemeEndTime: Long,
                    sourceType: Int,
                    sellTimeFrom: Long,
                    sellTimeTo: Long,
                    brandLogoFull: String,
                    cat1Id: Long,
                    cat2Id: Long,
                    cat3Id: Long,
                    cat1Name: String,
                    cat2Name: String,
                    cat3Name: String,
                    marketPrice: Double,
                    commission: Double,
                    status: Int,
                    destUrl: String,
                    vipPrice: Double,
                    haiTao: Int,
                    brandStoreSn: String,
                    brandId: Long,
                    discount: Double,
                    goodsThumbUrl: String,
                    commissionRate: String,
                    goodsMainPicture: String,
                    brandName: String,
                    storeId: String,
                    storeName: String,
                    ctime: String
                  )
