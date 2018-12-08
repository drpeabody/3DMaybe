#version 330

in vec2 TextureCood0;
in vec3 Normal0;
in vec3 WorldFragPos0;
in vec3 Tangent0;

out vec4 fragColor;

struct Light
{
    vec3 color;
    float DiffInten, MatSpecInten, SpecPower;
};
uniform struct DirectionalLight
{
    vec3 dir;
    float AmbiInten;
    Light light;
} dirLight;

uniform struct PointLight
{
    vec3 pos;
    float fallOff, cutOff;
    Light light;
} lights[8];

uniform int numLights;
uniform vec4 diffuseColor = vec4(0,0,0,0);
uniform vec4 emmisiveColor = vec4(0,0,0,0);
uniform sampler2D diffuseMap;
uniform sampler2D normalMap;
uniform sampler2D emmisiveMap;
uniform vec3 WorldEyePos0;

vec3 CalcBumpedNormal()
{
    vec3 Normal = normalize(Normal0);
    vec3 Tangent = normalize(Tangent0);
    Tangent = normalize(Tangent - dot(Tangent, Normal) * Normal);
    vec3 Bitangent = cross(Tangent, Normal);
    vec3 BumpMapNormal = texture2D(normalMap, TextureCood0.st).xyz;
    BumpMapNormal = 2.0 * BumpMapNormal - vec3(1.0, 1.0, 1.0);
    return normalize(mat3(Tangent, Bitangent, Normal) * BumpMapNormal);
}

vec4 calcLight(Light l, vec3 normal, vec3 dir)
{
    vec4 DiffColor = vec4(0,0,0,1), SpecColor = vec4(0,0,0,1);
    float DiffFactor = dot(normal, dir);

    if(DiffFactor > 0)
    {
        DiffColor = vec4(l.color * l.DiffInten * DiffFactor, 1);
        float SpecularFactor = dot(normalize(WorldEyePos0 - WorldFragPos0), normalize(reflect(dir, normal)));
        
        if (SpecularFactor > 0)
        {
            SpecularFactor = pow(SpecularFactor, l.SpecPower);
            SpecColor = vec4(l.color * l.MatSpecInten * SpecularFactor, 1);
        }
    }
    return (DiffColor + SpecColor);
}

vec4 calcDirectionalLight(vec3 normal)
{
    vec4 AmbiColor = vec4(dirLight.light.color,1) * dirLight.AmbiInten;
    return calcLight(dirLight.light, normal, dirLight.dir) + AmbiColor;
}

vec4 calcPointLight(int idx, vec3 normal)
{
    float dist = distance(WorldFragPos0, lights[idx].pos);

    if(dist > lights[idx].cutOff) return vec4(0,0,0,1);
    return calcLight(lights[idx].light, normal, normalize(WorldFragPos0 - lights[idx].pos)) * 
        smoothstep(lights[idx].cutOff, lights[idx].fallOff, dist);
}

void main(){
    
    vec3 normal = CalcBumpedNormal();
    vec4 lighting = calcDirectionalLight(normal);
    
    for(int i = 0; i < numLights; i++) lighting += calcPointLight(i, normal);

    fragColor = (diffuseColor + texture2D(diffuseMap, TextureCood0.st)) * lighting
            + texture2D(emmisiveMap, TextureCood0.st)
            + emmisiveColor;
}


