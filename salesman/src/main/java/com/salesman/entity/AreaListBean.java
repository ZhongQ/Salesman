package com.salesman.entity;

import java.util.List;

/**
 * 省市区列表实体
 * Created by LiHuai on 2016/6/30 0030.
 */
public class AreaListBean extends BaseBean {
    /**
     * id : 6
     * name : 广东省
     * pId : 1
     * regionLevel : 2
     * regionList : [{"id":76,"name":"广州","pId":6,"regionLevel":3,"regionList":[{"id":692,"name":"从化市","pId":76,"regionLevel":4},{"id":693,"name":"天河区","pId":76,"regionLevel":4},{"id":694,"name":"东山区","pId":76,"regionLevel":4},{"id":695,"name":"白云区","pId":76,"regionLevel":4},{"id":696,"name":"海珠区","pId":76,"regionLevel":4},{"id":697,"name":"荔湾区","pId":76,"regionLevel":4},{"id":698,"name":"越秀区","pId":76,"regionLevel":4},{"id":699,"name":"黄埔区","pId":76,"regionLevel":4},{"id":700,"name":"番禺区","pId":76,"regionLevel":4},{"id":701,"name":"花都区","pId":76,"regionLevel":4},{"id":702,"name":"增城区","pId":76,"regionLevel":4},{"id":703,"name":"从化区","pId":76,"regionLevel":4},{"id":704,"name":"市郊","pId":76,"regionLevel":4}]},{"id":77,"name":"深圳","pId":6,"regionLevel":3,"regionList":[{"id":705,"name":"福田区","pId":77,"regionLevel":4},{"id":706,"name":"罗湖区","pId":77,"regionLevel":4},{"id":707,"name":"南山区","pId":77,"regionLevel":4},{"id":708,"name":"宝安区","pId":77,"regionLevel":4},{"id":709,"name":"龙岗区","pId":77,"regionLevel":4},{"id":710,"name":"盐田区","pId":77,"regionLevel":4},{"id":3406,"name":"坪山区","pId":77,"regionLevel":4},{"id":3407,"name":"横岗区","pId":77,"regionLevel":4},{"id":3408,"name":"西乡","pId":77,"regionLevel":4},{"id":3409,"name":"南山餐饮","pId":77,"regionLevel":4},{"id":3410,"name":"南山转角","pId":77,"regionLevel":4},{"id":3411,"name":"龙岗转角","pId":77,"regionLevel":4},{"id":3412,"name":"体验专区","pId":77,"regionLevel":4},{"id":3413,"name":"福永","pId":77,"regionLevel":4},{"id":3414,"name":"石岩","pId":77,"regionLevel":4},{"id":3415,"name":"坂田","pId":77,"regionLevel":4},{"id":3416,"name":"龙华","pId":77,"regionLevel":4},{"id":3417,"name":"布吉","pId":77,"regionLevel":4},{"id":3418,"name":"光明区","pId":77,"regionLevel":4}]},{"id":79,"name":"东莞","pId":6,"regionLevel":3,"regionList":[{"id":714,"name":"南城区","pId":79,"regionLevel":4},{"id":715,"name":"东城区","pId":79,"regionLevel":4},{"id":716,"name":"万江区","pId":79,"regionLevel":4},{"id":717,"name":"莞城区","pId":79,"regionLevel":4},{"id":718,"name":"石龙镇","pId":79,"regionLevel":4},{"id":719,"name":"虎门镇","pId":79,"regionLevel":4},{"id":720,"name":"麻涌镇","pId":79,"regionLevel":4},{"id":721,"name":"道滘镇","pId":79,"regionLevel":4},{"id":722,"name":"石碣镇","pId":79,"regionLevel":4},{"id":723,"name":"沙田镇","pId":79,"regionLevel":4},{"id":724,"name":"望牛墩镇","pId":79,"regionLevel":4},{"id":725,"name":"洪梅镇","pId":79,"regionLevel":4},{"id":726,"name":"茶山镇","pId":79,"regionLevel":4},{"id":727,"name":"寮步镇","pId":79,"regionLevel":4},{"id":728,"name":"大岭山镇","pId":79,"regionLevel":4},{"id":729,"name":"大朗镇","pId":79,"regionLevel":4},{"id":730,"name":"黄江镇","pId":79,"regionLevel":4},{"id":731,"name":"樟木头","pId":79,"regionLevel":4},{"id":732,"name":"凤岗镇","pId":79,"regionLevel":4},{"id":733,"name":"塘厦镇","pId":79,"regionLevel":4},{"id":734,"name":"谢岗镇","pId":79,"regionLevel":4},{"id":735,"name":"厚街镇","pId":79,"regionLevel":4},{"id":736,"name":"清溪镇","pId":79,"regionLevel":4},{"id":737,"name":"常平镇","pId":79,"regionLevel":4},{"id":738,"name":"桥头镇","pId":79,"regionLevel":4},{"id":739,"name":"横沥镇","pId":79,"regionLevel":4},{"id":740,"name":"东坑镇","pId":79,"regionLevel":4},{"id":741,"name":"企石镇","pId":79,"regionLevel":4},{"id":742,"name":"石排镇","pId":79,"regionLevel":4},{"id":743,"name":"长安镇","pId":79,"regionLevel":4},{"id":744,"name":"中堂镇","pId":79,"regionLevel":4},{"id":745,"name":"高埗镇","pId":79,"regionLevel":4}]}]
     */

    public List<ProvinceBean> data;

    public static class ProvinceBean {
        public int id;
        public String name;
        public int pId;
        public int regionLevel;
        public List<CityBean> regionList;
    }

    public static class CityBean {
        public int id;
        public String name;
        public int pId;
        public int regionLevel;
        public List<AreaBean> regionList;
    }

    public static class AreaBean {
        public int id;
        public String name;
        public int pId;
        public int regionLevel;
    }
}
