package qm.qiaomeng.jackson

import qm.qiaomeng.model.{JDItem, PDDItem, TBItem, WPHItem}

import java.util
import scala.collection.mutable.ArrayBuffer

/**
 * @ClassName: ParseJson
 * @Description: 用jackson解析json
 * @Create
 * by: LinYoung
 * @Date: 2021/2/3 9:52
 */
object ParseJson {

  /**
   * string转Long
   *
   * @param str 要处理的id
   * @return 返回处理好的id
   */
  private def stringToLong(str: String): Long = if (str.matches("[{].*[}]")) java.lang.Long.valueOf(str.split(":")(1).split("\"")(1)) else if (str.matches("[\"].*[\"]")) java.lang.Long.valueOf(str.split("\"")(1)) else java.lang.Long.valueOf(str)

  /**
   * string转double
   *
   * @param str 要转换的字符串
   * @return 转换结果
   */
  private def stringToDouble(str: String): Double = java.lang.Double.valueOf(str.replaceAll("\"", ""))

  /**
   * string转int
   *
   * @param str 要转换的字符串
   * @return 转换结果
   */
  private def stringToInt(str: String): Int = java.lang.Integer.valueOf(str.replaceAll("\"", ""))


  private def stringToString(str: String): String = str.replaceAll("\"", "")

  /**
   * 解析淘宝字段
   *
   * @param line 要解析的字段
   * @return 返回淘宝类
   */
  def parseTBJson(line: String): TBItem = {
    // 解析一级key
    val itemMap: util.HashMap[String, Any] = Jackson.autoParseJsonFirst(line)
    val imageMap = Jackson.autoParseJsonFirst(itemMap.getOrDefault("small_images", "").toString)

    // 对样例类一一赋值
    TBItem(
      source = "淘宝",
      itemId = stringToLong(itemMap.getOrDefault("item_id", 0).toString),
      itemDescription = stringToString(itemMap.getOrDefault("item_description", "").toString),
      itemTitle = stringToString(itemMap.getOrDefault("title", "").toString),
      itemShortTitle = stringToString(itemMap.getOrDefault("short_title", "").toString),
      pictureURL = stringToString(itemMap.getOrDefault("pict_url", "").toString),
      smallImages = stringToString(imageMap.getOrDefault("string", "").toString).replaceAll("\\[", "").replaceAll("]", "").split(","),
      price = stringToDouble(itemMap.getOrDefault("reserve_price", 0.0).toString),
      categoryId = stringToLong(itemMap.getOrDefault("category_id", 0).toString),
      categoryName = stringToString(itemMap.getOrDefault("category_name", "").toString),
      categoryId1 = stringToLong(itemMap.getOrDefault("level_one_category_id", 0).toString),
      categoryName1 = stringToString(itemMap.getOrDefault("level_one_category_name", "").toString),
      postFee = stringToString(itemMap.getOrDefault("real_post_fee", "").toString),
      MKT = stringToString(itemMap.getOrDefault("include_mkt", "").toString),
      whiteImage = stringToString(itemMap.getOrDefault("white_image", "").toString),
      tkTotalSales = stringToInt(itemMap.getOrDefault("tk_total_sales", 0).toString),
      DXJHInfo = stringToString(itemMap.getOrDefault("info_dxjh", "").toString),
      DXJH = stringToString(itemMap.getOrDefault("include_dxjh", "").toString),
      commissionType = stringToString(itemMap.getOrDefault("commission_type", "").toString),
      commissionRate = stringToString(itemMap.getOrDefault("commission_rate", "").toString),
      volume = stringToInt(itemMap.getOrDefault("volume", 0).toString),
      itemURL = stringToString(itemMap.getOrDefault("item_url", "").toString),
      City = stringToString(itemMap.getOrDefault("provcity", "").toString),
      couponId = stringToString(itemMap.getOrDefault("coupon_id", "").toString),
      couponInfo = stringToString(itemMap.getOrDefault("coupon_info", "").toString),
      couponTotalCount = stringToInt(itemMap.getOrDefault("coupon_total_count", 0).toString),
      couponRemainCount = stringToDouble(itemMap.getOrDefault("coupon_remain_count", 0).toString),
      zkFinalPrice = stringToDouble(itemMap.getOrDefault("zk_final_price", 0).toString),
      presaleDeposit = stringToString(itemMap.getOrDefault("presale_deposit", "").toString),
      presaleStartTime = stringToLong(itemMap.getOrDefault("presale_start_time", 0).toString),
      presaleEndTime = stringToLong(itemMap.getOrDefault("presale_end_time", 0).toString),
      presaleTailStartTime = stringToLong(itemMap.getOrDefault("presale_tail_start_time", 0).toString),
      presaleTailEndTime = stringToLong(itemMap.getOrDefault("presale_tail_end_time", 0).toString),
      shopTitle = stringToString(itemMap.getOrDefault("shop_title", "").toString),
      sellerId = stringToLong(itemMap.getOrDefault("seller_id", 0).toString),
      userType = stringToString(itemMap.getOrDefault("user_type", "").toString),
      DSR = stringToString(itemMap.getOrDefault("shop_dsr", "").toString),
      Nick = stringToString(itemMap.getOrDefault("nick", "").toString),
      xId = stringToString(itemMap.getOrDefault("x_id", "").toString),
      URL = stringToString(itemMap.getOrDefault("url", "").toString),
      superiorBrand = stringToInt(itemMap.getOrDefault("superior_brand", 0).toString),
      ctime = stringToString(itemMap.getOrDefault("update_time", "").toString)
    )
  }

  /**
   * 解析京东字段
   *
   * @param line 要解析的字段
   * @return 返回京东样例类
   */
  def parseJDJson(line: String): JDItem = {
    val itemMap: util.HashMap[String, Any] = Jackson.autoParseJsonFirst(line)
    val priceInfo = itemMap.getOrDefault("priceInfo", "").toString
    val priceMap = Jackson.autoParseJsonFirst(priceInfo)
    val categoryInfo = itemMap.getOrDefault("categoryInfo", "").toString
    val categoryMap = Jackson.autoParseJsonFirst(categoryInfo)
    val pinGouInfo = itemMap.getOrDefault("pinGouInfo", "").toString
    val pinGouMap = Jackson.autoParseJsonFirst(pinGouInfo)
    val shopInfo = itemMap.getOrDefault("shopInfo", "").toString
    val shopMap = Jackson.autoParseJsonFirst(shopInfo)
    val couponInfo = itemMap.getOrDefault("couponInfo", "").toString
    val couponMap = Jackson.autoParseJsonFirst(couponInfo)
    val commissionInfo = itemMap.getOrDefault("commissionInfo", "").toString
    val commissionMap = Jackson.autoParseJsonFirst(commissionInfo)
    val imageInfo = itemMap.getOrDefault("imageInfo", "").toString
    val imageMap = Jackson.autoParseJsonFirst(imageInfo)
    val images = imageMap.getOrDefault("imageList", "").toString
    val imageList = Jackson.string2Array(images)

    val imageArray = new ArrayBuffer[String]()
    for (image <- imageList) {
      val img = Jackson.autoParseJsonFirst(image.toString)
      val imageStr = img.getOrDefault("url", "").toString.replaceAll("\"", "")
      imageArray.append(imageStr)
    }

    val videoInfo = itemMap.getOrDefault("videoInfo", "").toString
    val videoMap = Jackson.autoParseJsonFirst(videoInfo)
    JDItem(
      source = "京东",
      skuId = stringToLong(itemMap.getOrDefault("skuId", 0).toString),
      skuName = stringToString(itemMap.getOrDefault("skuName", "").toString),
      spuId = stringToLong(itemMap.getOrDefault("skuId", 0).toString),
      brandName = stringToString(itemMap.getOrDefault("brandName", "").toString),
      brandCode = stringToString(itemMap.getOrDefault("brandCode", "").toString),
      commentCount = stringToLong(itemMap.getOrDefault("comments", 0).toString),
      goodCommentRate = stringToDouble(itemMap.getOrDefault("goodCommentsShare", 0).toString),
      inOrderCount30Days = stringToLong(itemMap.getOrDefault("inOrderCount30Days", 0).toString),
      inOrderComm30Days = stringToDouble(itemMap.getOrDefault("inOrderComm30Days", 0).toString),
      owner = stringToString(itemMap.getOrDefault("owner", "").toString),
      videoInfo = stringToString(videoMap.getOrDefault("videoList", "").toString),
      images = imageArray.toArray,
      deliveryType = stringToInt(itemMap.getOrDefault("deliveryType", 0).toString),
      forbidTypes = stringToString(itemMap.getOrDefault("forbidTypes", "").toString),
      materialUrl = stringToString(itemMap.getOrDefault("materialUrl", "").toString),
      historyPriceDay = stringToInt(priceMap.getOrDefault("historyPriceDay", 0).toString),
      lowestCouponPrice = stringToDouble(priceMap.getOrDefault("lowestCouponPrice", 0).toString),
      price = stringToDouble(priceMap.getOrDefault("price", 0).toString),
      lowestPrice = stringToDouble(priceMap.getOrDefault("lowestPrice", 0).toString),
      lowestPriceType = stringToInt(priceMap.getOrDefault("lowestPriceType", 0).toString),
      cid1Name = stringToString(categoryMap.getOrDefault("cid1Name", "").toString),
      cid2Name = stringToString(categoryMap.getOrDefault("cid2Name", "").toString),
      cid3Name = stringToString(categoryMap.getOrDefault("cid3Name", "").toString),
      cid1Id = stringToInt(categoryMap.getOrDefault("cid1Id", 0).toString),
      cid2Id = stringToInt(categoryMap.getOrDefault("cid2Id", 0).toString),
      cid3Id = stringToInt(categoryMap.getOrDefault("cid3Id", 0).toString),
      pingouPrice = stringToDouble(pinGouMap.getOrDefault("pingouPrice", 0).toString),
      pingouTmCount = stringToLong(pinGouMap.getOrDefault("pingouTmCount", 0).toString),
      pingouUrl = stringToString(pinGouMap.getOrDefault("pingouUrl", "").toString),
      pingouStartTime = stringToLong(pinGouMap.getOrDefault("pingouStartTime", 0).toString),
      pingouEndTime = stringToLong(pinGouMap.getOrDefault("pingouEndTime", 0).toString),
      shopName = stringToString(shopMap.getOrDefault("shopName", "").toString),
      shopId = stringToLong(shopMap.getOrDefault("shopId", 0).toString),
      shopLabel = stringToString(shopMap.getOrDefault("shopLabel", "").toString),
      shopLevel = stringToDouble(shopMap.getOrDefault("shopLevel", 0).toString),
      userEvaluateScore = stringToString(shopMap.getOrDefault("userEvaluateScore", "").toString),
      commentFactorScoreRankGrade = stringToString(shopMap.getOrDefault("commentFactorScoreRankGrade", "").toString),
      logisticsLvyueScore = stringToString(shopMap.getOrDefault("logisticsLvyueScore", "").toString),
      logisticsFactorScoreRankGrade = stringToString(shopMap.getOrDefault("logisticsFactorScoreRankGrade", "").toString),
      afterServiceScore = stringToString(shopMap.getOrDefault("afterServiceScore", "").toString),
      afsFactorScoreRankGrade = stringToString(shopMap.getOrDefault("afsFactorScoreRankGrade", "").toString),
      scoreRankRate = stringToString(shopMap.getOrDefault("scoreRankRate", "").toString),
      couponList = stringToString(couponMap.getOrDefault("couponList", "").toString),
      commission = stringToDouble(commissionMap.getOrDefault("commission", 0).toString),
      commissionShare = stringToDouble(commissionMap.getOrDefault("commissionShare", 0).toString),
      couponCommission = stringToDouble(commissionMap.getOrDefault("couponCommission", 0).toString),
      plusCommissionShare = stringToDouble(commissionMap.getOrDefault("plusCommissionShare", 0).toString),
      isLock = stringToInt(commissionMap.getOrDefault("isLock", 0).toString),
      startTime = stringToLong(commissionMap.getOrDefault("startTime", 0).toString),
      endTime = stringToLong(commissionMap.getOrDefault("endTime", 0).toString),
      ctime = stringToString(itemMap.getOrDefault("update_time", "").toString)
    )
  }

  /**
   * 解析拼多多字段
   *
   * @param line 要解析的字段
   * @return 返回拼多多样例类
   */
  def parsePDDJson(line: String): PDDItem = {
    val itemMap = Jackson.autoParseJsonFirst(line)
    PDDItem(source = "拼多多",
      itemId = stringToLong(itemMap.getOrDefault("goods_id", "").toString),
      itemName = stringToString(itemMap.getOrDefault("goods_name", "").toString),
      itemURL = stringToString(itemMap.getOrDefault("goods_image_url", "").toString),
      itemThumbnailURL = stringToString(itemMap.getOrDefault("goods_thumbnail_url", "").toString),
      itemDesc = stringToString(itemMap.getOrDefault("goods_desc", "").toString),
      itemSign = stringToString(itemMap.getOrDefault("goods_sign", "").toString),
      descTxt = stringToString(itemMap.getOrDefault("desc_txt", "").toString),
      servTxt = stringToString(itemMap.getOrDefault("serv_txt", "").toString),
      lgstTxt = stringToString(itemMap.getOrDefault("lgst_txt", "").toString),
      saleCount = stringToString(itemMap.getOrDefault("sales_tip", "").toString),
      labelId = stringToLong(itemMap.getOrDefault("opt_id", 0).toString),
      labelName = stringToString(itemMap.getOrDefault("opt_name", "").toString),
      labelIds = itemMap.getOrDefault("opt_ids", "").toString,
      categoryIds = itemMap.getOrDefault("cat_ids", "").toString,
      shopCPS = stringToInt(itemMap.getOrDefault("mall_cps", 0).toString),
      shopId = stringToLong(itemMap.getOrDefault("mall_id", 0).toString),
      shopName = stringToString(itemMap.getOrDefault("mall_name", "").toString),
      shopType = stringToInt(itemMap.getOrDefault("merchant_type", 0).toString),
      shopCouponCount = stringToInt(itemMap.getOrDefault("mall_coupon_total_quantity", 0).toString),
      shopCouponStartTime = stringToLong(itemMap.getOrDefault("mall_coupon_start_time", 0).toString),
      shopCouponEndTime = stringToLong(itemMap.getOrDefault("mall_coupon_end_time", 0).toString),
      shopHasCoupon = stringToString(itemMap.getOrDefault("has_mall_coupon", "").toString),
      shopCouponId = stringToLong(itemMap.getOrDefault("mall_coupon_id", 0).toString),
      shopCouponRemain = stringToInt(itemMap.getOrDefault("mall_coupon_remain_quantity", 0).toString),
      shopZkCoupon = stringToInt(itemMap.getOrDefault("mall_coupon_discount_pct", 0).toString),
      shopCouponMinAmount = stringToInt(itemMap.getOrDefault("mall_coupon_min_order_amount", 0).toString),
      shopCouponMaxAmount = stringToInt(itemMap.getOrDefault("mall_coupon_max_discount_amount", 0).toString),
      minNormalPrice = stringToInt(itemMap.getOrDefault("min_normal_price", 0).toString),
      minGroupPrice = stringToInt(itemMap.getOrDefault("min_group_price", 0).toString),
      hasCoupon = stringToString(itemMap.getOrDefault("has_coupon", "").toString),
      couponDisCount = itemMap.getOrDefault("coupon_discount", "").toString,
      couponCount = stringToInt(itemMap.getOrDefault("coupon_total_quantity", 0).toString),
      couponRemain = stringToInt(itemMap.getOrDefault("coupon_remain_quantity", 0).toString),
      couponStartTime = stringToLong(itemMap.getOrDefault("coupon_start_time", 0).toString),
      couponEndTime = stringToLong(itemMap.getOrDefault("coupon_end_time", 0).toString),
      couponMinAmount = stringToInt(itemMap.getOrDefault("coupon_min_order_amount", 0).toString),
      promotionRate = stringToInt(itemMap.getOrDefault("promotion_rate", 0).toString),
      planType = stringToInt(itemMap.getOrDefault("plan_type", 0).toString),
      ZSId = stringToLong(itemMap.getOrDefault("zs_duo_id", 0).toString),
      predictPromotionRate = stringToLong(itemMap.getOrDefault("predict_promotion_rate", 0).toString),
      searchId = stringToString(itemMap.getOrDefault("search_id", "").toString),
      serviceTags = itemMap.getOrDefault("service_tags", 0).toString,
      unifiedTags = stringToString(itemMap.getOrDefault("unified_tags", "").toString),
      sceneAuth = itemMap.getOrDefault("only_scene_auth", "").toString,
      ctime = stringToString(itemMap.getOrDefault("update_time", "").toString)
    )
  }

  /**
   * 解析唯品会字段
   *
   * @param line 要解析的字段
   */
  def parseWPHJson(line: String): WPHItem = {
    val itemMap = Jackson.autoParseJsonFirst(line)
    WPHItem(
      source = "唯品会",
      goodsId = stringToLong(itemMap.getOrDefault("goodsId", 0).toString),
      goodsName = stringToString(itemMap.getOrDefault("goodsName", "").toString),
      spuId = stringToString(itemMap.getOrDefault("spuId", "").toString),
      schemeStartTime = stringToLong(itemMap.getOrDefault("schemeStartTime", 0).toString),
      schemeEndTime = stringToLong(itemMap.getOrDefault("schemeEndTime", 0).toString),
      sourceType = stringToInt(itemMap.getOrDefault("sourceType", 0).toString),
      sellTimeFrom = stringToLong(itemMap.getOrDefault("sellTimeFrom", 0).toString),
      sellTimeTo = stringToLong(itemMap.getOrDefault("sellTimeTo", 0).toString),
      brandLogoFull = stringToString(itemMap.getOrDefault("brandLogoFull", "").toString),
      cat1Id = stringToLong(itemMap.getOrDefault("cat1stId", 0).toString),
      cat2Id = stringToLong(itemMap.getOrDefault("cat2ndId", 0).toString),
      cat3Id = stringToLong(itemMap.getOrDefault("categoryId", 0).toString),
      cat1Name = stringToString(itemMap.getOrDefault("cat1stName", "").toString),
      cat2Name = stringToString(itemMap.getOrDefault("cat2ndName", "").toString),
      cat3Name = stringToString(itemMap.getOrDefault("categoryName", "").toString),
      marketPrice = stringToDouble(itemMap.getOrDefault("marketPrice", 0).toString),
      commission = stringToDouble(itemMap.getOrDefault("commission", 0).toString),
      status = stringToInt(itemMap.getOrDefault("status", 0).toString),
      destUrl = stringToString(itemMap.getOrDefault("destUrl", "").toString),
      vipPrice = stringToDouble(itemMap.getOrDefault("vipPrice", 0).toString),
      haiTao = stringToInt(itemMap.getOrDefault("haiTao", 0).toString),
      brandStoreSn = stringToString(itemMap.getOrDefault("brandStoreSn", "").toString),
      brandId = stringToLong(itemMap.getOrDefault("brandId", 0).toString),
      discount = stringToDouble(itemMap.getOrDefault("discount", 0).toString),
      goodsThumbUrl = stringToString(itemMap.getOrDefault("goodsThumbUrl", "").toString),
      commissionRate = stringToString(itemMap.getOrDefault("commissionRate", "").toString),
      goodsMainPicture = stringToString(itemMap.getOrDefault("goodsMainPicture", "").toString),
      brandName = stringToString(itemMap.getOrDefault("brandName", "").toString),
      storeId = stringToString(itemMap.getOrDefault("storeId", "").toString),
      storeName = stringToString(itemMap.getOrDefault("storeName", "").toString),
      ctime = stringToString(itemMap.getOrDefault("update_time", "").toString)
    )
  }


  def main(args: Array[String]): Unit = {
    val jd =
      """
        |{
        |	"_id": {
        |		"$oid": "5fd9f6a20e46e42a31407253"
        |	},
        |	"priceInfo": {
        |		"historyPriceDay": 180,
        |		"lowestCouponPrice": 38.0,
        |		"price": 38.0,
        |		"lowestPrice": 38.0,
        |		"lowestPriceType": 1
        |	},
        |	"inOrderComm30Days": 0.0,
        |	"categoryInfo": {
        |		"cid3Name": "墙贴/装饰贴",
        |		"cid2Name": "家装软饰",
        |		"cid1Name": "家居日用",
        |		"cid1": 1620,
        |		"cid2": 11158,
        |		"cid3": 11965
        |	},
        |	"videoInfo": {},
        |	"owner": "p",
        |	"inOrderCount30Days": 0,
        |	"pingGouInfo": {},
        |	"shopInfo": {
        |		"userEvaluateScore": "9.11",
        |		"shopName": "美简艺家居专营店",
        |		"shopLabel": "1",
        |		"shopId": 10061628,
        |		"scoreRankRate": "98.15",
        |		"afterServiceScore": "9.58",
        |		"shopLevel": 4.9,
        |		"commentFactorScoreRankGrade": "高",
        |		"logisticsLvyueScore": "9.76",
        |		"logisticsFactorScoreRankGrade": "高",
        |		"afsFactorScoreRankGrade": "高"
        |	},
        |	"comments": 4,
        |	"couponInfo": {
        |		"couponList": []
        |	},
        |	"isJdSale": 0,
        |	"skuId": {
        |		"$numberLong": "63170906149"
        |	},
        |	"update_time": "2020-12-16 19:59:30",
        |	"imageInfo": {
        |		"imageList": [{
        |			"url": "https://img14.360buyimg.com/pop/jfs/t1/95697/2/2902/40233/5dd8ee91Ef6e1a687/bba3eb11d0f15e14.jpg"
        |		}, {
        |			"url": "https://img14.360buyimg.com/pop/jfs/t1/96659/14/4430/308974/5de6ffd2E44cf7167/3e99b1530b66c760.jpg"
        |		}, {
        |			"url": "https://img14.360buyimg.com/pop/jfs/t1/89929/39/4410/46757/5de6ffe2E802b07b5/b0a125b2b87596c2.jpg"
        |		}, {
        |			"url": "https://img14.360buyimg.com/pop/jfs/t1/87382/23/4390/278477/5de70032E3b834f2e/2f45587abf7d5e30.jpg"
        |		}, {
        |			"url": "https://img14.360buyimg.com/pop/jfs/t1/103023/13/4404/197867/5de7005aEdb16f782/99514d895b9c15a5.jpg"
        |		}],
        |		"whiteImage": "https://img14.360buyimg.com/pop/jfs/t1/101181/27/4359/45324/5de706d9Eb2539c27/4d1cd8ab4d27147a.png"
        |	},
        |	"deliveryType": 0,
        |	"brandCode": "447088",
        |	"skuName": "反光醒目小心站台间隙定做 高铁小心站台间隙火车站站台警示带 走向标识PVC地贴汽车站地面指引导视贴纸 黄黑境界线(条纹 高粘性)120x20cm",
        |	"forbidTypes": [0],
        |	"materialUrl": "item.jd.com/63170906149.html",
        |	"pinGouInfo": {},
        |	"spuid": {
        |		"$numberLong": "63170906149"
        |	},
        |	"goodCommentsShare": 100.0,
        |	"isHot": 0,
        |	"brandName": "彬策",
        |	"commissionInfo": {
        |		"isLock": 0,
        |		"couponCommission": 0.76,
        |		"commission": 0.76,
        |		"commissionShare": 2.0,
        |		"startTime": {
        |			"$numberLong": "1563724800000"
        |		},
        |		"endTime": {
        |			"$numberLong": "32472201599000"
        |		},
        |		"plusCommissionShare": 2.0
        |	}
        |}
        |""".stripMargin

    val wph =
      """
        |{
        |	"_id": {
        |		"$oid": "5fe1a03a27db86f9e0f78bee"
        |	},
        |	"spuId": "SPU-08D7C6CA800000BF",
        |	"schemeStartTime": {
        |		"$numberLong": "1590027839000"
        |	},
        |	"marketPrice": "399.00",
        |	"sourceType": 0,
        |	"sellTimeFrom": {
        |		"$numberLong": "1538027186000"
        |	},
        |	"brandLogoFull": "http://a.vpimg3.com/upload/brandcool/0/de9aaacf54dc49a289c44cda5e30e301/10004913/primary.png",
        |	"cat1stId": 356,
        |	"commission": "1.08",
        |	"cat2ndId": 653,
        |	"categoryId": 660,
        |	"status": 1,
        |	"update_time": "2020-12-22 15:28:58",
        |	"cat2ndName": "个人护理家电",
        |	"categoryName": "理发器",
        |	"destUrl": "https://m.vip.com/product-1710617120-6918773852415846016.html",
        |	"vipPrice": "108.00",
        |	"haiTao": 0,
        |	"brandStoreSn": "10004913",
        |	"brandId": 1710617120,
        |	"discount": "0.30",
        |	"goodsId": "6918773852415846016",
        |	"goodsName": "理发器电推剪全身水洗专业成人儿童电动理发剪剃头电推子6305",
        |	"storeInfo": {
        |		"storeId": "ST00000",
        |		"storeName": "唯品自营"
        |	},
        |	"goodsThumbUrl": "https://a.vpimg4.com/upload/merchandise/pdcvis/106710/2020/0509/58/2ab3cc26-c450-4a08-b557-08e051d58dcc_750x750_50.jpg",
        |	"sellTimeTo": {
        |		"$numberLong": "1735660800000"
        |	},
        |	"commissionRate": "1",
        |	"goodsMainPicture": "https://a.vpimg2.com/upload/merchandise/pdcvis/106710/2020/0509/58/2ab3cc26-c450-4a08-b557-08e051d58dcc.jpg",
        |	"brandName": "雷瓦",
        |	"schemeEndTime": {
        |		"$numberLong": "2145888000000"
        |	},
        |	"cat1stName": "家用电器"
        |}
        |""".stripMargin

    val tb =
      """
        |{
        |	"_id": {
        |		"$oid": "5fd063712e47d4b8f4ec80db"
        |	},
        |	"provcity": "上海",
        |	"commission_type": "MKT",
        |	"level_one_category_id": 201230407,
        |	"small_images": {
        |		"string": ["https://img.alicdn.com/i3/2206420244516/O1CN01bd8hFH1jENBa0V7BT_!!2206420244516.jpg", "https://img.alicdn.com/i4/2206420244516/O1CN01MFjL8P1jENBhIQXjQ_!!2206420244516.jpg", "https://img.alicdn.com/i3/2206420244516/O1CN01S7X0vf1jENEaqtlbQ_!!2206420244516.jpg", "https://img.alicdn.com/i4/2206420244516/O1CN01PO5O3H1jEND9y0bRV_!!2206420244516.jpg"]
        |	},
        |	"user_type": 1,
        |	"coupon_id": "25fe6e5da13941a3a07988ef837ec6fc",
        |	"coupon_share_url": "//uland.taobao.com/coupon/edetail?e=R4CyiOIURZ8NfLV8niU3RxrSI%2FOabn6qNg4Gqf8CT4AKuDLwELihnYGmjrnfjmGy4hWNPuolGQ0fXVJ2T%2BbwoqJlfwCOjU6TiL%2FoyCXBd2p48hM0EgLpNVwBskfs732H0PeSMXFk9kHoOTsYz%2F5zrRaP2bhL284I7Q0iq2fF2x37dPWckGeyiAoKStFKcQJDFF6O9STo8Qg7mmMWJDI59WUFFgz3RBVH\u0026\u0026app_pvid=59590_33.8.85.21_692_1607492464863\u0026ptl=floorId:2836;app_pvid:59590_33.8.85.21_692_1607492464863;tpp_pvid:100_11.14.220.39_5775_801607492464865702\u0026xId=2uJt72d2TF239O2j14973p79vapCtLZBAdf7hLeoDZBVL6SVlATmepZcHIaND2Kac0CjjWJkPTUXSeeMR7dJ58HzzuUfKAJGkbFV4q3ex0DV\u0026union_lens=lensId%3AMAPI%401607492465%4021085515_0779_176460471d2_342a%4001",
        |	"shop_dsr": 49432,
        |	"presale_end_time": 0,
        |	"tk_total_sales": "0",
        |	"item_description": "",
        |	"coupon_info": "满3000元减350元",
        |	"item_url": "https://detail.tmall.com/item.htm?id=602011479008",
        |	"white_image": "",
        |	"commission_rate": "1200",
        |	"presale_tail_end_time": 0,
        |	"coupon_total_count": 2000,
        |	"nick": "名流健康旗舰店",
        |	"include_mkt": "true",
        |	"level_one_category_name": "体检/医疗保障卡",
        |	"tk_total_commi": "0",
        |	"coupon_end_time": "2020-12-31",
        |	"zk_final_price": "3350",
        |	"presale_tail_start_time": 0,
        |	"update_time": "2020-12-09 13:41:05",
        |	"x_id": "2uJt72d2TF239O2j14973p79vapCtLZBAdf7hLeoDZBVL6SVlATmepZcHIaND2Kac0CjjWJkPTUXSeeMR7dJ58HzzuUfKAJGkbFV4q3ex0DV",
        |	"presale_start_time": 0,
        |	"real_post_fee": "0.00",
        |	"coupon_start_fee": "3000",
        |	"shop_title": "名流健康旗舰店",
        |	"kuadian_promotion_info": "[每300减40]",
        |	"coupon_amount": "350",
        |	"superior_brand": "0",
        |	"coupon_remain_count": 1998,
        |	"item_id": {
        |		"$numberLong": "602011479008"
        |	},
        |	"coupon_start_time": "2020-11-17",
        |	"presale_deposit": "0",
        |	"category_name": "老年体检",
        |	"volume": 0,
        |	"seller_id": {
        |		"$numberLong": "2206420244516"
        |	},
        |	"url": "//s.click.taobao.com/t?e=m%3D2%26s%3DxEEPUbFW87QcQipKwQzePOeEDrYVVa64r4ll3HtqqoxyINtkUhsv0DNz%2BaAw7ww3dAatf5mt3XFM4LUhqo7nfL2gbRZbE7ESCSykfW%2BtXrcv9ZUG3Aeu2BFws0R1Jv2%2B1GPduzu4oNq8JhuVYXYU9FFKtrx7J3I3EiM%2FlSG%2FbZS91a%2Bv8AN9B4UgzzdeAxH6JVG1eS0MucTGDF1NzTQoPw%3D%3D\u0026scm=null\u0026pvid=100_11.14.220.39_5775_801607492464865702\u0026app_pvid=59590_33.8.85.21_692_1607492464863\u0026ptl=floorId:2836;originalFloorId:2836;pvid:100_11.14.220.39_5775_801607492464865702;app_pvid:59590_33.8.85.21_692_1607492464863\u0026xId=2uJt72d2TF239O2j14973p79vapCtLZBAdf7hLeoDZBVL6SVlATmepZcHIaND2Kac0CjjWJkPTUXSeeMR7dJ58HzzuUfKAJGkbFV4q3ex0DV\u0026union_lens=lensId%3AMAPI%401607492465%4021085515_0779_176460471d2_342a%4001",
        |	"include_dxjh": "false",
        |	"title": "名流健康成人高端全面体检套餐青年中老年体检上海体检合肥体检",
        |	"num_iid": {
        |		"$numberLong": "602011479008"
        |	},
        |	"short_title": "名流健康成人高端全面体检套餐体检",
        |	"reserve_price": "4250",
        |	"info_dxjh": "{}",
        |	"category_id": 50019102,
        |	"pict_url": "https://img.alicdn.com/bao/uploaded/i3/2206420244516/O1CN01YRahyu1jENH6pvvwR_!!2206420244516.jpg"
        |}
        |
        |""".stripMargin

    val pdd =
      """
        |
        |{
        |	"_id": {
        |		"$oid": "5fd1bdee5f263cabfa0eeeb1"
        |	},
        |	"has_coupon": true,
        |	"zs_duo_id": 0,
        |	"coupon_end_time": 1609430399,
        |	"serv_txt": "高",
        |	"plan_type": 2,
        |	"promotion_rate": 100,
        |	"merchant_type": 1,
        |	"opt_ids": [24080, 24081, 23010, 9907, 4, 5285, 24085, 9911, 9098],
        |	"coupon_min_order_amount": 100,
        |	"mall_cps": 1,
        |	"lgst_txt": "高",
        |	"goods_id": {
        |		"$numberLong": "160515869937"
        |	},
        |	"coupon_start_time": 1604851200,
        |	"opt_name": "母婴",
        |	"goods_desc": "卡片奥特曼卡片金卡全套经典版20弹荣耀版豪华版收藏册收集册卡牌",
        |	"goods_thumbnail_url": "https://t00img.yangkeduo.com/goods/images/2020-08-02/1b673bae1497f986c600c5c4ac81892f.jpeg",
        |	"goods_image_url": "https://img.pddpic.com/mms-material-img/2020-08-02/e8071bf0-0333-4373-8dc5-be9db9ec75bc.jpg.a.jpeg",
        |	"mall_coupon_discount_pct": 0,
        |	"mall_id": 379666069,
        |	"mall_coupon_max_discount_amount": 0,
        |	"mall_coupon_id": 0,
        |	"search_id": "1c58d78866c124c0c8825e24b0b8aa4b03a",
        |	"predict_promotion_rate": 100,
        |	"coupon_remain_quantity": 4300,
        |	"update_time": "2020-12-10 14:19:26",
        |	"only_scene_auth": false,
        |	"goods_name": "卡片奥特曼卡片金卡全套经典版20弹荣耀版豪华版收藏册收集册卡牌",
        |	"coupon_discount": 100,
        |	"mall_coupon_start_time": 0,
        |	"mall_coupon_total_quantity": 0,
        |	"goods_sign": "c9z2hzUzMmVGKXoRwfDY4TGXCW-S_JsGGI2zsC",
        |	"cat_ids": [20645, 20654, 20655],
        |	"mall_name": "童鑫儿童玩具店",
        |	"desc_txt": "高",
        |	"mall_coupon_end_time": 0,
        |	"min_normal_price": 1700,
        |	"category_name": "母婴",
        |	"opt_id": 23010,
        |	"sales_tip": "1708",
        |	"has_mall_coupon": false,
        |	"unified_tags": ["退货包运费"],
        |	"service_tags": [12],
        |	"min_group_price": 1600,
        |	"mall_coupon_remain_quantity": 0,
        |	"coupon_total_quantity": 5000,
        |	"category_id": 23010,
        |	"mall_coupon_min_order_amount": 0
        |}
        |
        |""".stripMargin


    //    println(parseJDJson(jd))
    //    println(parseWPHJson(wph))
    //    println(parseTBJson(tb))
    //    println(parsePDDJson(pdd))
    println(Jackson.bean2String(parsePDDJson(pdd)))
    println(Jackson.bean2String(parseTBJson(tb)))
    println(Jackson.bean2String(parseWPHJson(wph)))
    println(Jackson.bean2String(parseJDJson(jd)))


  }
}