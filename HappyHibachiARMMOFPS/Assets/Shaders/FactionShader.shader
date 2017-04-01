Shader "Unlit/FactionShader"
{
	Properties
	{
		_MainTex ("Texture", 2D) = "white" {}
		_FactionMask ("Faction Mask", 2D) = "white" {}
		_ParagonTex ("Paragon Faction Texture", 2D) = "white" {}
		_SlayerTex ("Slayer Faction Texture", 2D) = "white" {}
		_HunterTex ("Hunter Faction Texture", 2D) = "white" {}
		_FactionList("Faction List", Vector) = (0,0,0,0)
	}
	SubShader
	{
		Tags { "RenderType"="Opaque" }
		LOD 100
		
		Pass
		{
			CGPROGRAM
// Upgrade NOTE: excluded shader from DX11, OpenGL ES 2.0 because it uses unsized arrays
#pragma exclude_renderers d3d11 gles
			#pragma vertex vert
			#pragma fragment frag
			// make fog work
			#pragma multi_compile_fog
			
			#include "UnityCG.cginc"

			struct appdata
			{
				float4 vertex : POSITION;
				float2 uv : TEXCOORD0;
			};

			struct v2f
			{
				float2 uv : TEXCOORD0;
				UNITY_FOG_COORDS(1)
				float4 vertex : SV_POSITION;
			};

			sampler2D _MainTex;
			float4 _MainTex_ST;
			half4 _FactionList;
			sampler2D _FactionMask;
			sampler2D _ParagonTex[] : register(s0);
			sampler2D _SlayerTex[] : register(s1);
			sampler2D _HunterTex[] : register(s2);

			
			
			v2f vert (appdata v)
			{
				v2f o;
				o.vertex = UnityObjectToClipPos(v.vertex);
				o.uv = TRANSFORM_TEX(v.uv, _MainTex);
				UNITY_TRANSFER_FOG(o,o.vertex);
				return o;
			}
			
			fixed4 frag (v2f i) : SV_Target
			{
				// sample the texture
				fixed4 col = tex2D(_MainTex, i.uv);

				// Here we're gonna check the texture color, then
				// change the appearance based on the faction in control there
				fixed4 fmap = tex2D(_FactionMask, i.uv);

				// We have four indices: 0, 1, 2, 3
				// If the texture is red, we want index 0
				// Green, 1, Blue, 2, White, 3.
				// Assuming the texture must be one of these four colors,
				// 1 x green + 2 x blue will evaluate to the right index
				fixed findex = 1 * fmap.y + 2 * fmap.z;
				// This works because if neither green nor blue is 1, we assume red
				// and if both are 1, we can assume red is 1 too, and hence white

				// Now the faction in power is identified by _FactionList[findex]
				// Just need to transform it into a texture now...
				fixed4 newCol = tex2D(ParagonTex[findex], i.uv);
				
				newCol = (newCol + col) / 2.0;

				// apply fog
				UNITY_APPLY_FOG(i.fogCoord, newCol);
				// Blend the original color with the faction texture
				return newCol;
			}
			ENDCG
		}
	}
}
