void main() {
    //convert the current position by mod 50
    vec2 pos = mod(gl_FragCoord.xy, vec2(50.0));

    vec4 val = vec4(0.0);

    //checkerboard
    if((pos.x < 25.0 && pos.y < 25.0) || (pos.x>25.0 && pos.y > 25.0) ) {
        val = vec4(1,1,1,1);
    } else {
        val = vec4(0,0,0,1);
    }

    gl_FragColor = val;
}