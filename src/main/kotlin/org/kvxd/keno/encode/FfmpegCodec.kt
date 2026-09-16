package org.kvxd.keno.encode

enum class FfmpegCodec(
    internal val encoderName: String?,
    internal val arguments: List<String>,
) {
    AUTO(null, emptyList()),
    H264(listOf("libx264", "-preset", "medium", "-crf", "18")),
    H264_NVIDIA(listOf("h264_nvenc", "-preset", "p4", "-cq", "19")),
    H264_INTEL(listOf("h264_qsv", "-preset", "faster", "-global_quality", "19")),
    H264_AMD(listOf("h264_amf", "-quality", "balanced", "-usage", "transcoding", "-rc", "cqp")),
    H264_APPLE(listOf("h264_videotoolbox", "-q:v", "65"));

    private constructor(specification: List<String>) : this(
        encoderName = specification.first(),
        arguments = specification.drop(1),
    )
}
