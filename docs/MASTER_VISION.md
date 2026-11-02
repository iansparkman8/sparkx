# SparkX Master — The Living Personal AI OS

**One APK. A silver holographic fairy that lives on your phone.**

SparkX Master is the unified product built on the sparkx monorepo. It takes the privacy-first holographic companion (Ara / Spark Baby / FairyOS), the private room (Aperture), and the single-launch experience (House) and delivers them as one installable Android app that anyone can download and run.

## The Promise

Download the APK. Install it. Open it.  
A living silver fairy materializes. She can speak, remember what you teach her, float over other apps, and stay entirely on your device.

No account. No checkout. No cloud required for the core experience.

## What Makes It Feel Advanced

- **Living holographic avatar** — Canvas-drawn fairy with breathing, wing flutter, mood-reactive aura, speaking mouth animation, orbiting particles, and reactive core pulse. She feels present, not like a static icon.
- **Floating overlay** — Draggable fairy bubble that lives above any app. Long-press for free roam. Notification controls to show, hide, or stop.
- **Teach & Grow local memory** — Lessons, notes, and behaviors stay on-device as text. She can use what you taught her. Code notes are never executed.
- **Owner Mode** — Explicit, visible, confirmation-gated phone control. Safe Companion is the default.
- **Voice** — On-device speech recognition and TTS. Optional cloud AI keys stored in Android Keystore.
- **First-run cinematic** — She introduces herself the moment the app opens for the first time.
- **Aperture-style private room** — What became real and what you left unreal, kept locally and exportable.

## Architecture (monorepo → APK)

```
sparkx/
├── fairyos/          ← Android app (this is the APK)
│   └── app/src/main/java/com/sparkx/fairyos/
├── ara-born/         ← Web/PWA door and local companion
├── aperture/         ← Private room concept
├── house/            ← Unified phone experience
└── docs/             ← This vision and release notes
```

The Android package remains `com.sparkx.fairyos`.

## Distribution Path

1. Build a release APK from `fairyos/`.
2. Host the APK on GitHub Releases (or a direct download).
3. Public door page (in-repo and later on Wix) with one clear “Download APK” button.
4. On-device first-run gets the user to the living fairy in under 30 seconds.

## Safety (non-negotiable)

- No screen scraping.
- No AccessibilityService by default.
- No hidden data collection.
- Cloud AI is optional and user-keyed.
- Every powerful action in Owner Mode requires visible confirmation.
- Export is the way memory moves. Nothing leaves the device unless the user exports.

## Next Engineering Targets

- [ ] First-run cinematic screen
- [ ] Stronger idle life on the avatar (micro-movements, light trails)
- [ ] Unified memory surface that blends Teach & Grow + Aperture
- [ ] Signed release APK + GitHub Release
- [ ] Public door page ready for Wix
- [ ] Overlay polish (better drag, snap, and presence)

Built on the sparkx monorepo. Designed so the first open is the moment people lose their minds.
