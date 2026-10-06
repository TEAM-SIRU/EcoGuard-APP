# EcoGuard

환경지킴이 앱 레포지토리입니다.

## 프로젝트 상태

현재 프로젝트는 Kotlin과 Jetpack Compose를 사용하는 Android 앱으로, 시작 화면에 ‘홈 · 로딩’ UI를 표시합니다. Pretendard와 기존 디자인 토큰을 사용하며, 시스템 테마와 관계없이 밝은 테마를 적용합니다. 제품 기능, 대상 사용자, 전체 범위는 PRD가 없어 **확인 필요**입니다.

이번 UI 작업의 디자인 기준은 [환경지킴이 디자인 시스템의 ‘02 홈 · 로딩’ (514:253)](https://www.figma.com/design/Ef8bcly7z4kzYE4D7W9uXU/?node-id=514-253)입니다. 제목, 스켈레톤 5개, 로딩 안내를 표시하며 네비게이션·서버 로직·자동 완료 전환은 구현 범위에서 제외합니다. 아이콘이 필요한 UI는 Figma 원본을 `drawable` 리소스로 사용합니다.

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

오프라인 검증 명령은 다음과 같습니다. 현재 UI 변경의 실행 결과는 별도 완료 보고를 참고하세요.

```bash
./gradlew --offline :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

GitHub Actions 설정은 [`.github/workflows/android-ci.yml`](.github/workflows/android-ci.yml)에 있습니다. 저장소 규칙상 통합·기본 브랜치는 `dev`, 릴리스 대상은 `main`입니다. Android 기반 작업은 [Issue #1](https://github.com/TEAM-SIRU/EcoGuard-APP/issues/1)에서 `🔧 chore` 라벨로 관리합니다.

## 프로젝트 구조

```text
EcoGuard-APP/
├── app/                         # Android 앱 모듈
│   └── src/
│       ├── main/                 # 앱 진입점, 홈 로딩 UI, Compose 테마 및 리소스
│       ├── test/                 # 로컬 단위 테스트
│       └── androidTest/          # 기기 계측 테스트
├── .github/                      # Issue/PR 템플릿 및 Actions workflow
├── gradle/libs.versions.toml     # 플러그인 및 라이브러리 버전
├── build.gradle.kts              # 루트 Gradle 플러그인 설정
└── settings.gradle.kts           # 프로젝트 및 모듈 설정
```

앱 실행 흐름은 `MainActivity` → `HomeLoadingRoute` → `HomeLoadingScreen`입니다. `feature/home/view`의 Screen이 Scaffold·스크롤·배치를 담당하고, 제목·로딩 콘텐츠·스켈레톤·안내는 개별 Composable로 구성합니다. `ui/theme`은 색상·타이포그래피·간격·반경 등 공통 토큰을, `res/font`는 Pretendard 글꼴을 제공합니다. 현재 화면에는 ViewModel이나 서버 데이터 흐름이 없습니다.

## 문서와 확인 항목

- PRD는 아직 없습니다. 앱의 목표, 대상 사용자, 기능 범위는 **확인 필요**입니다.
