fun textFieldValueRegex(value: String, maxLength: String = "9"): String {
    val regex = Regex("-?(\\d{0,$maxLength}(?:\\.\\d{0,2})?)?")
        .find(value)?.value.orEmpty()

    var replacedText = regex

    if (regex.length >= 2) {
        if (regex.first() == '0' && regex[1] != '.') {
            replacedText = regex.replace("0", "")
        }
    }

    if (regex.isNotEmpty()) {
        if (regex.first() == '.') {
            replacedText =
                regex.replace("${regex.first()}", "")
        }
    }

    return replacedText
}