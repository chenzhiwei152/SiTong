data class MusicDetailBean(
        var message: String,
        var data: Data,
        var statuscode: Int
) {

    data class Data(
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
            var score: List<Score>,
            var icon: String
    ) {

        data class Score(
                var sort: Double,
                var numbered_music_middle: Any,
                var sound_type: Int,
                var right_str: String,
                var end_second: List<Double>,
                var string: String,
                var left_str: String,
                var jianzipu: String,
                var percent: String,
                var isend: Int,
                var bpm: Int,
                var numbered_music: String,
                var numbered_music_up: Any,
                var jianziwidth: Double,
                var islinefeed: Int,
                var start_second: List<Double>,
                var duration: Double,
                var jianziheight: Double,
                var symbol: List<Symbol>,
                var sound_type_desc: String
        ) {

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
}