
uniform vec2 gradientCenter;
uniform float gradientRadius;
uniform vec4 startColor;

void main() {
    //simple radial gradient
    //float gradientRadius = 30.0;
    //vec4 startColor = vec4(0,0,0,1);
    vec4 endColor = vec4(1,0,1,1);

    //get distance from gradient center
    float len = length(gl_FragCoord.xy - gradientCenter);
    //divide by radius to get value from 0-1
    float gradientCoeff = len / gradientRadius;
    //find the mix between the start and end colors
    gl_FragColor = mix(startColor,endColor,gradientCoeff);

    //gl_FragColor = val;
}