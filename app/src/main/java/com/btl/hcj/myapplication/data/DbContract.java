package com.btl.hcj.myapplication.data;

import android.provider.BaseColumns;

public class DbContract {

    public static final class ShelterInfoEntry implements BaseColumns {
        public static final String TABLE_NAME = "shelter_info";
        public static final String COLUMN_DESCRIPTION = "민방위대피시설명";
        public static final String COLUMN_CLASS = "민방위대피시설구분";
        public static final String COLUMN_ADDRESS_ROAD = "소재지도로명주소";
        public static final String COLUMN_ADDRESS_LAND = "소재지지번주소";
        public static final String COLUMN_LATITUDE = "위도";
        public static final String COLUMN_LONGITUDE = "경도";
        public static final String COLUMN_SIZE = "민방위대피시설면적";
        public static final String COLUMN_CAPACITY = "대피가능인원수";
        public static final String COLUMN_AVAILABLE = "개방여부";
        public static final String COLUMN_TYPE = "평시활용유형";
        public static final String COLUMN_OFFICE_NUMBER = "관리기관전화번호";
        public static final String COLUMN_OFFICE_NAME = "관리기관명";
        public static final String COLUMN_UPDATE_DATE = "데이터기준일자";
    }
}
