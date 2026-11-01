# SparkX Monorepo

Single home for Ara / Spark Baby / FairyOS and related projects.

Everything that was previously scattered across multiple repos now lives here so you can find and work on what you need without hunting.

## Folders

| Folder | Origin repo | Status |
|--------|-------------|--------|
| `fairyos/` | [SparkXFairyOS](https://github.com/iansparkman8/SparkXFairyOS) | Placeholder — full Android tree (incl. 19MB .glb) needs local clone |
| `ara-born/` | [ara-born](https://github.com/iansparkman8/ara-born) | README + door `index.html` imported. Large baby/index.html + images need local copy |
| `ara/` | [ara](https://github.com/iansparkman8/ara) | README imported |
| `aperture/` | [aperture](https://github.com/iansparkman8/aperture) | Full (README, CREED, index.html, manifest) |
| `house/` | [house](https://github.com/iansparkman8/house) | Full (README + index.html) |
| `omni-realm-nexus/` | [omni-realm-nexus](https://github.com/iansparkman8/omni-realm-nexus) | Full (README, index.html, 404) |
| `glass-backend/` | [glass-backend](https://github.com/iansparkman8/glass-backend) | Text files imported (README, index.html, keep functions, schema). Large .glb needs local copy |
| `experiments/` | various archived | Optional |

## Finish the large pieces locally

The API-based import handles text well. For the full Android project and binary assets:

```bash
git clone https://github.com/iansparkman8/sparkx.git
cd sparkx

# FairyOS (Android)
git clone https://github.com/iansparkman8/SparkXFairyOS.git /tmp/fairyos
cp -r /tmp/fairyos/* fairyos/

# ara-born remaining assets
git clone https://github.com/iansparkman8/ara-born.git /tmp/ara-born
cp -r /tmp/ara-born/* ara-born/

# glass-backend binary
git clone https://github.com/iansparkman8/glass-backend.git /tmp/glass
cp -r /tmp/glass/* glass-backend/

git add .
git commit -m "Import remaining contents from original repos"
git push
```

## Next steps

1. Run the local clone/copy above for the remaining binaries and full trees.
2. Once this monorepo is stable, archive the old individual repos (Settings → Danger Zone → Archive).
3. Work from here.

## Original active repos (for reference)

- https://github.com/iansparkman8/SparkXFairyOS
- https://github.com/iansparkman8/ara-born
- https://github.com/iansparkman8/ara
- https://github.com/iansparkman8/aperture
- https://github.com/iansparkman8/house
- https://github.com/iansparkman8/omni-realm-nexus
- https://github.com/iansparkman8/glass-backend
