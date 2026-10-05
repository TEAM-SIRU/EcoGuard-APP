# EcoGuard

환경지킴이 앱 레포지토리입니다.

## 프로젝트 상태

현재 프로젝트는 Kotlin과 Jetpack Compose를 사용하는 Android 앱의 시작 템플릿입니다. 실행 화면은 `MainActivity`의 기본 인사말 화면입니다. 제품 기능, 대상 사용자, 구체적인 범위는 PRD가 없어 **확인 필요**입니다.

## 기술 스택

| 항목 | 설정 |
|---|---|
| 모듈 | `:app` |
| 언어 및 UI | Kotlin 2.2.10, Jetpack Compose, Material 3 |
| Android Gradle Plugin | 9.2.1 |
| Gradle Wrapper | 9.4.1 |
| Gradle daemon JDK | 21 |
| Java source/target | 11 |
| Application ID | `com.nativelap.ecoguard` |
| Android SDK | `compileSdk 37`, `minSdk 34`, `targetSdk 36` |

## 시작하기

### 요구사항

- JDK 21 (Gradle daemon toolchain)
- Android SDK 및 Android 빌드 도구

### 빌드

저장소를 받은 뒤 디버그 APK를 빌드합니다.

```bash
git clone https://github.com/TEAM-SIRU/EcoGuard-APP.git
cd EcoGuard-APP
./gradlew :app:assembleDebug
```

단위 테스트와 Android Lint는 다음 명령으로 실행할 수 있습니다.

```bash
./gradlew :app:testDebugUnitTest :app:lintDebug
```

로컬 오프라인 검증 결과: **성공** (51개 작업).

```bash
./gradlew --offline :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

GitHub Actions 설정은 [`.github/workflows/android-ci.yml`](.github/workflows/android-ci.yml)에 있습니다. GitHub 기본 브랜치는 `main`, 개발 통합 브랜치는 `dev`입니다. Android 기반 작업은 [Issue #1](https://github.com/TEAM-SIRU/EcoGuard-APP/issues/1)에서 `🔧 chore` 라벨로 관리합니다.

## 프로젝트 구조

```text
EcoGuard-APP/
├── app/                         # Android 앱 모듈
│   └── src/
│       ├── main/                 # 앱 진입점, Compose 테마 및 리소스
│       ├── test/                 # 로컬 단위 테스트
│       └── androidTest/          # 기기 계측 테스트
├── .github/                      # Issue/PR 템플릿 및 Actions workflow
├── gradle/libs.versions.toml     # 플러그인 및 라이브러리 버전
├── build.gradle.kts              # 루트 Gradle 플러그인 설정
└── settings.gradle.kts           # 프로젝트 및 모듈 설정
```

## 문서와 확인 항목

- PRD는 아직 없습니다. 앱의 목표, 대상 사용자, 기능 범위는 **확인 필요**입니다.
