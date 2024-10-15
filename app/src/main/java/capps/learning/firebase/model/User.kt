package capps.learning.firebase.model

data class User(
    var id: String,
    var fName: String,
    var lName: String,
    var username: String
) {
    // No-argument constructor required for Firebase
    constructor() : this("", "", "", "")
}
