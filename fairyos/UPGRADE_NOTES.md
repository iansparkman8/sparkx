# Deep Core Upgrades (v7.1+)

- High-quality 3D body (`ara-body-new.glb`) preferred by `assets/body/index.html`
- New `BodyViewerScreen.kt` Compose WebView for the real textured/rigged model
- New `CoreMind.kt` local-first reply engine that recalls Teach & Grow memory
- Command router now falls through to CoreMind instead of a generic "I heard" response
- Memory search already present; CoreMind uses it for contextual answers

The large GLB binary must be committed separately if not already present.
