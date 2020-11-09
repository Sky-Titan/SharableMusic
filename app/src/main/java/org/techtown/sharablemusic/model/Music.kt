package org.techtown.sharablemusic.model

data class Music(var mId : Long, var albumId : Long, var title : String, var artist : String, var album : String, var duration : Long, var dataPath : String) {
     // mId 오디오 고유 ID
    //AlbumId 오디오 앨범아트 ID
    //Title 타이틀 정보
    //Artist 아티스트 정보
    // Album 앨범 정보
    // Duration  재생시간
    // DataPath 실제 데이터 위치

}