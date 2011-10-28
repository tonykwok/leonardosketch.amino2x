uniform sampler2D tex;
void main() {
    vec2 coord = gl_TexCoord[0].st;
    vec4 color = texture2D(tex,coord);
    gl_FragColor = color;
}