#version 330

uniform sampler2D texture;
in float TexDominance;
in vec2 texCood0;
in vec4 color0;

void main(){
    vec4 v = texture2D(texture, texCood0.st);
    gl_FragColor = TexDominance*v + color0;
}