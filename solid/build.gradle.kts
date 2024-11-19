dependencies {
    compileOnly(libs.lombok)

    annotationProcessor(libs.lombok)

    implementation(libs.snakeyaml)
    implementation(libs.bundles.logging)
}