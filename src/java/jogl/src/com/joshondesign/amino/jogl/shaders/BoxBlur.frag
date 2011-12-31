uniform sampler2D tex;

//uniform vec4 color;

const float d1 = 1.0/25.0;
const float d2 = 2.0/25.0;
const float d3 = 3.0/25.0;
const float d4 = 4.0/25.0;
const float d5 = 5.0/25.0;
const float blurSize = 3.0;

void main() {
    vec4 sum = vec4(0.0);
    vec2 vTexCoord = gl_TexCoord[0].st;
    vec4 color = texture2D(tex,vTexCoord);


    sum += texture2D(tex, vec2(vTexCoord.x - 4.0*blurSize, vTexCoord.y)) * d1;
    sum += texture2D(tex, vec2(vTexCoord.x - 3.0*blurSize, vTexCoord.y)) * d2;
    sum += texture2D(tex, vec2(vTexCoord.x - 2.0*blurSize, vTexCoord.y)) * d3;
    sum += texture2D(tex, vec2(vTexCoord.x - blurSize, vTexCoord.y)) * d4;
    sum += texture2D(tex, vec2(vTexCoord.x, vTexCoord.y)) * d5;
    sum += texture2D(tex, vec2(vTexCoord.x + blurSize, vTexCoord.y)) * d4;
    sum += texture2D(tex, vec2(vTexCoord.x + 2.0*blurSize, vTexCoord.y)) * d3;
    sum += texture2D(tex, vec2(vTexCoord.x + 3.0*blurSize, vTexCoord.y)) * d2;
    sum += texture2D(tex, vec2(vTexCoord.x + 4.0*blurSize, vTexCoord.y)) * d1;

    gl_FragColor = 1.5*sum;
    //gl_FragColor = 0.1*color;

    //turn the pixel gray but preserve the alpha
    //gl_FragColor = vec4(0.5,0.5,0.5,color.a);

    vec4 c1 =  texture2D(tex, vec2(gl_TexCoord[0].s-0.005, gl_TexCoord[0].t-0.005));
    vec4 c2 =  texture2D(tex, vec2(gl_TexCoord[0].s+0.005, gl_TexCoord[0].t-0.005));
    vec4 c3 =  texture2D(tex, vec2(gl_TexCoord[0].s-0.005, gl_TexCoord[0].t+0.005));
    vec4 c4 =  texture2D(tex, vec2(gl_TexCoord[0].s+0.005, gl_TexCoord[0].t+0.005));
    float al = (c1.a + c2.a + c3.a + c4.a)/4.0;
    gl_FragColor = vec4(color.r,color.g,color.b,al);
    //gl_FragColor = vec4(0,0,0,al);

    //turn the pixel magenta
    //gl_FragColor = vec4(1,0,1,1);
}