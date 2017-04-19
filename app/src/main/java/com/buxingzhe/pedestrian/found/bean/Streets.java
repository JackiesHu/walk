package com.buxingzhe.pedestrian.found.bean;

import java.util.List;

/**
 * Created by hasee on 2017/4/18.
 */

public class Streets {
    /**
     * type : Feature
     * properties : {"ws":0.86,"name":"内环快速路","rdType":0,"rdCarN":7,"rdBusN":0,"rdBikeGL":0,"rdBikeN":0,"rdWalkWid":0,"rdBikeWid":0,"rdIlpkWk":0,"rdIlpkBk":0,"rdIlpkOt":0,"rdCbpk":0,"rdFtpk":0,"check":0}
     * geometry : {
     *      "coordinates":[
     *          {
     *          "type":"LineString",
     *          "coordinates":
     *              [
     *                  {"x":106.560423,"y":29.613119,"coordinates":[106.560423,29.613119],"type":"Point"},
     *                  {"x":106.560603,"y":29.613078,"coordinates":[106.560603,29.613078],"type":"Point"},
     *                  {"x":106.560628,"y":29.613072,"coordinates":[106.560628,29.613072],"type":"Point"},
     *                  {"x":106.561303,"y":29.612925,"coordinates":[106.561303,29.612925],"type":"Point"},
     *                  {"x":106.561333,"y":29.612919,"coordinates":[106.561333,29.612919],"type":"Point"},
     *                  {"x":106.562172,"y":29.612745,"coordinates":[106.562172,29.612745],"type":"Point"},
     *                  {"x":106.562221,"y":29.612735,"coordinates":[106.562221,29.612735],"type":"Point"},
     *                  {"x":106.562588,"y":29.612659,"coordinates":[106.562588,29.612659],"type":"Point"},
     *                  {"x":106.562916,"y":29.612599,"coordinates":[106.562916,29.612599],"type":"Point"},
     *                  {"x":106.56293,"y":29.612596,"coordinates":[106.56293,29.612596],"type":"Point"},
     *                  {"x":106.563075,"y":29.612563,"coordinates":[106.563075,29.612563],"type":"Point"},
     *                  {"x":106.563193,"y":29.612539,"coordinates":[106.563193,29.612539],"type":"Point"},
     *                  {"x":106.564312,"y":29.612301,"coordinates":[106.564312,29.612301],"type":"Point"}
     *              ]
     *            }
     *        ],
     *        "type":"MultiLineString"
     *    }
     */

    private String type;
    private PropertiesBean properties;
    private GeometryBean geometry;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PropertiesBean getProperties() {
        return properties;
    }

    public void setProperties(PropertiesBean properties) {
        this.properties = properties;
    }

    public GeometryBean getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryBean geometry) {
        this.geometry = geometry;
    }

    public static class PropertiesBean {
        /**
         * ws : 0.86
         * name : 内环快速路
         * rdType : 0.0
         * rdCarN : 7.0
         * rdBusN : 0.0
         * rdBikeGL : 0.0
         * rdBikeN : 0.0
         * rdWalkWid : 0.0
         * rdBikeWid : 0.0
         * rdIlpkWk : 0.0
         * rdIlpkBk : 0.0
         * rdIlpkOt : 0.0
         * rdCbpk : 0.0
         * rdFtpk : 0.0
         * check : 0.0
         */

        private double ws;
        private String name;
        private double rdType;
        private double rdCarN;
        private double rdBusN;
        private double rdBikeGL;
        private double rdBikeN;
        private double rdWalkWid;
        private double rdBikeWid;
        private double rdIlpkWk;
        private double rdIlpkBk;
        private double rdIlpkOt;
        private double rdCbpk;
        private double rdFtpk;
        private double check;

        public double getWs() {
            return ws;
        }

        public void setWs(double ws) {
            this.ws = ws;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getRdType() {
            return rdType;
        }

        public void setRdType(double rdType) {
            this.rdType = rdType;
        }

        public double getRdCarN() {
            return rdCarN;
        }

        public void setRdCarN(double rdCarN) {
            this.rdCarN = rdCarN;
        }

        public double getRdBusN() {
            return rdBusN;
        }

        public void setRdBusN(double rdBusN) {
            this.rdBusN = rdBusN;
        }

        public double getRdBikeGL() {
            return rdBikeGL;
        }

        public void setRdBikeGL(double rdBikeGL) {
            this.rdBikeGL = rdBikeGL;
        }

        public double getRdBikeN() {
            return rdBikeN;
        }

        public void setRdBikeN(double rdBikeN) {
            this.rdBikeN = rdBikeN;
        }

        public double getRdWalkWid() {
            return rdWalkWid;
        }

        public void setRdWalkWid(double rdWalkWid) {
            this.rdWalkWid = rdWalkWid;
        }

        public double getRdBikeWid() {
            return rdBikeWid;
        }

        public void setRdBikeWid(double rdBikeWid) {
            this.rdBikeWid = rdBikeWid;
        }

        public double getRdIlpkWk() {
            return rdIlpkWk;
        }

        public void setRdIlpkWk(double rdIlpkWk) {
            this.rdIlpkWk = rdIlpkWk;
        }

        public double getRdIlpkBk() {
            return rdIlpkBk;
        }

        public void setRdIlpkBk(double rdIlpkBk) {
            this.rdIlpkBk = rdIlpkBk;
        }

        public double getRdIlpkOt() {
            return rdIlpkOt;
        }

        public void setRdIlpkOt(double rdIlpkOt) {
            this.rdIlpkOt = rdIlpkOt;
        }

        public double getRdCbpk() {
            return rdCbpk;
        }

        public void setRdCbpk(double rdCbpk) {
            this.rdCbpk = rdCbpk;
        }

        public double getRdFtpk() {
            return rdFtpk;
        }

        public void setRdFtpk(double rdFtpk) {
            this.rdFtpk = rdFtpk;
        }

        public double getCheck() {
            return check;
        }

        public void setCheck(double check) {
            this.check = check;
        }
    }

    public static class GeometryBean {
        /**
         * coordinates : [{"type":"LineString","coordinates":[{"x":106.560423,"y":29.613119,"coordinates":[106.560423,29.613119],"type":"Point"},{"x":106.560603,"y":29.613078,"coordinates":[106.560603,29.613078],"type":"Point"},{"x":106.560628,"y":29.613072,"coordinates":[106.560628,29.613072],"type":"Point"},{"x":106.561303,"y":29.612925,"coordinates":[106.561303,29.612925],"type":"Point"},{"x":106.561333,"y":29.612919,"coordinates":[106.561333,29.612919],"type":"Point"},{"x":106.562172,"y":29.612745,"coordinates":[106.562172,29.612745],"type":"Point"},{"x":106.562221,"y":29.612735,"coordinates":[106.562221,29.612735],"type":"Point"},{"x":106.562588,"y":29.612659,"coordinates":[106.562588,29.612659],"type":"Point"},{"x":106.562916,"y":29.612599,"coordinates":[106.562916,29.612599],"type":"Point"},{"x":106.56293,"y":29.612596,"coordinates":[106.56293,29.612596],"type":"Point"},{"x":106.563075,"y":29.612563,"coordinates":[106.563075,29.612563],"type":"Point"},{"x":106.563193,"y":29.612539,"coordinates":[106.563193,29.612539],"type":"Point"},{"x":106.564312,"y":29.612301,"coordinates":[106.564312,29.612301],"type":"Point"}]}]
         * type : MultiLineString
         */

        private String type;
        private List<CoordinatesBeanX> coordinates;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<CoordinatesBeanX> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<CoordinatesBeanX> coordinates) {
            this.coordinates = coordinates;
        }

        public static class CoordinatesBeanX {
            /**
             * type : LineString
             * coordinates : [{"x":106.560423,"y":29.613119,"coordinates":[106.560423,29.613119],"type":"Point"},{"x":106.560603,"y":29.613078,"coordinates":[106.560603,29.613078],"type":"Point"},{"x":106.560628,"y":29.613072,"coordinates":[106.560628,29.613072],"type":"Point"},{"x":106.561303,"y":29.612925,"coordinates":[106.561303,29.612925],"type":"Point"},{"x":106.561333,"y":29.612919,"coordinates":[106.561333,29.612919],"type":"Point"},{"x":106.562172,"y":29.612745,"coordinates":[106.562172,29.612745],"type":"Point"},{"x":106.562221,"y":29.612735,"coordinates":[106.562221,29.612735],"type":"Point"},{"x":106.562588,"y":29.612659,"coordinates":[106.562588,29.612659],"type":"Point"},{"x":106.562916,"y":29.612599,"coordinates":[106.562916,29.612599],"type":"Point"},{"x":106.56293,"y":29.612596,"coordinates":[106.56293,29.612596],"type":"Point"},{"x":106.563075,"y":29.612563,"coordinates":[106.563075,29.612563],"type":"Point"},{"x":106.563193,"y":29.612539,"coordinates":[106.563193,29.612539],"type":"Point"},{"x":106.564312,"y":29.612301,"coordinates":[106.564312,29.612301],"type":"Point"}]
             */

            private String type;
            private List<CoordinatesBean> coordinates;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<CoordinatesBean> getCoordinates() {
                return coordinates;
            }

            public void setCoordinates(List<CoordinatesBean> coordinates) {
                this.coordinates = coordinates;
            }

            public static class CoordinatesBean {
                /**
                 * x : 106.560423
                 * y : 29.613119
                 * coordinates : [106.560423,29.613119]
                 * type : Point
                 */

                private double x;
                private double y;
                private String type;
                private List<Double> coordinates;

                public double getX() {
                    return x;
                }

                public void setX(double x) {
                    this.x = x;
                }

                public double getY() {
                    return y;
                }

                public void setY(double y) {
                    this.y = y;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public List<Double> getCoordinates() {
                    return coordinates;
                }

                public void setCoordinates(List<Double> coordinates) {
                    this.coordinates = coordinates;
                }
            }
        }
    }
}
