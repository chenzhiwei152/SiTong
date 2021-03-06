package com.sevenstringedzithers.sitong.mvp.model.bean

import java.io.Serializable


data class MusicDetailBean(

            var iscollection: Boolean,
            var video_url: String,
            var hasright: Boolean,
            var introduce: String,
            var from_detail: String,
            var melody: String,
            var desc: String,
            var levelcode: Int,
            var name: String,
            var copyright: String,
            var level: String,
            var url: String,
            var size: Double,
            var delay: Double,
            var score: ArrayList<Score>,
            var icon: String
    ) : Serializable {

        data class Score(
                var sort: Double,
                var numbered_music_middle: String,
                var sound_type: Int,
                var right_str: String,
                var end_second: List<Double>,
                var string: String,
                var left_str: String,
                var jianzipu: String,
                var percent: String,
                var toposition: String,
                var fromposition: String,
                var portamento:Boolean,
                var overtone:Boolean,
                var isend: Int,
                var bpm: Int,
                var toline:String,
                var numbered_music: String,
                var numbered_music_up: String,
                var jianziwidth: Double,
                var islinefeed: Int,
                var start_second: List<Double>,
                var duration: Double,
                var jianziheight: Double,
                var symbol: List<Symbol>,
                var sound_type_desc: String
        ) : Serializable {

            data class Symbol(
                    var sort: Double,
                    var positioncode: Int,
                    var name: String,
                    var param: String,
                    var namecode: Int,
                    var position: String
            )
        }
}