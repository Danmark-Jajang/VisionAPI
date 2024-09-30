package com.example.visionapi

import android.content.Context
import android.widget.Toast

//c : Context에는 CameraActivity의 Context가 들어가있음
class SearchItem(var c: Context) {
    lateinit var temp : String
    var context : Context = c
    fun searchItem(s : OCRText){
        //test용 Toast, @@@@@@@나중에 지우세요@@@@@@@
        Toast.makeText(context, "Search ${s.s}", Toast.LENGTH_SHORT).show()

        //TODO - DB검색 기능 추가하고 Return 기능 추가, 새로운 Activity를 띄워서 결과 띄울것인지?
        // 지금 OCRText data의 String은 띄어쓰기가 들어가있어 검색에 어려움이 있을거 같은데 모든 공백을 지워야 하나?
        // (OCR검색할 때 띄어쓰기 다 지워서 저장 or 검색하기 직전에 띄어쓰기 삭제)
    }
}