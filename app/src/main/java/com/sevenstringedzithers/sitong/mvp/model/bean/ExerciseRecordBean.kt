package com.sevenstringedzithers.sitong.mvp.model.bean


data class ExerciseRecordBean(
        var date: ArrayList<Date>,
        var music: ArrayList<Music>
) {

    data class Music(
            var name: String,
            var musicid: String
    )


    data class Date(
            var date: String,
            var duration: String
    )
}