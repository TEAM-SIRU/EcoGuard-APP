# EcoGuard

환경지킴이 앱 레포지토리입니다.

## 프로젝트 상태

현재 프로젝트는 Kotlin과 Jetpack Compose를 사용하는 Android 앱입니다. 서버 연동·ViewModel·화면 전환 없이 화면별 정적 UI를 구현한 단계이며, 앱을 실행하면 홈 화면의 로딩 상태가 표시됩니다. 제품 기능, 대상 사용자, 전체 범위는 PRD가 없어 **확인 필요**입니다.

### 디자인 및 구현 기준

[Issue #12](https://github.com/TEAM-SIRU/EcoGuard-APP/issues/12)에서 Figma [‘App Screens · Redesign 2’ 페이지 (823:574)](https://www.figma.com/design/Ef8bcly7z4kzYE4D7W9uXU/?node-id=823-574)의 58개 화면·상태를 반영했습니다. 추가 Preview 상태 4개를 포함해 62개 상태를 렌더링 검증 대상으로 관리합니다.

- 글꼴: Noto Sans KR Regular·Medium·Bold를 `res/font`에서 사용합니다. [OFL 라이선스](licenses/NotoSansKR-OFL.txt)를 포함합니다.
- 색상: Primary `#55B580`, 기본 CTA에 사용하는 Secondary `#30875B`, 배경 `#FAFBFA`, 흰색 보조 버튼과 테두리를 적용합니다. CTA 색상은 Figma 원본 PNG의 실제 색상을 기준으로 합니다.
- 수치: 기본 CTA 높이 48dp, FAB 크기 60dp 등 Figma 원본 크기·간격을 디자인 토큰과 화면에 반영합니다.
- 에셋: 원본 SVG를 Android VectorDrawable로 변환하고 청소구역 도면을 로컬 이미지로 사용합니다. 원본·리소스 매핑은 [에셋 문서](docs/redesign-assets.md)를 참고하세요.
- 신청 입력: 학번·이름은 읽기 전용입니다. 동기는 Unicode 코드 포인트 기준 최대 200자로 제한하고 카운터를 표시합니다. 빈 내용·공백만 있는 내용·제출 중에는 신청 버튼을 비활성화합니다. 입력은 저장 가능한 Compose 상태로 유지하며 외부 상태 변경을 반영합니다.
- 반응형: 기본 콘텐츠 최대폭은 600dp입니다. 홈·청소구역은 840dp 이상에서 2열·최대폭 960dp를 사용하며, 나머지 화면은 최대폭 600dp를 유지합니다.
- 테마: 밝은 테마를 사용합니다. 다크 디자인 확정 전까지 다크 색상 구성도 밝은 테마와 같습니다.
- 범위: 기존 Route·Screen·UiState·화면 이벤트 구조를 유지하는 정적 UI 작업입니다. 화면 이벤트를 제공하지만 실제 화면 전환·서버 요청·카메라 촬영은 연결하지 않았습니다.

### 구현 범위

| 패키지 (`feature/*`) | 구현한 UI |
|---|---|
| `login` | 로그인, 교사 계정 안내 |
| `home` | 홈(로딩·불러오기 실패·활동 제외·콘텐츠 상태), 오늘/이번 주 청소, 모집·공지 카드 |
| `recruitment` | 모집 공고, 신청, 신청 결과, 불러오기 실패 |
| `area` | 청소 구역 |
| `verification` | 청소 인증 흐름(안내·촬영·사진 확인·제출 완료·업로드 실패·인증 시간 종료·인증 불가 시트·주말/방학 안내), 인증 결과 |
| `appeal` | 이의신청 작성·제출 완료·내역·결과 |
| `activity` | 활동 기록, 월 선택 다이얼로그 |
| `menu` | 전체 메뉴, 로그아웃·회원탈퇴 확인 다이얼로그 |
| `notice` | 공지 불러오기 실패 |

앱 시작 시 시스템 스플래시(흰 배경과 새싹 아이콘)를 표시합니다. 플랫폼 제약으로 Figma의 '환경지킴이' 글자는 스플래시에 표시하지 않습니다.

후속 작업(현재 범위 제외): 화면 전환, 서버 연동, ViewModel 연결, 실제 촬영. 구체적인 일정과 순서는 **확인 필요**입니다.

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

포맷 적용과 검사는 다음 명령을 사용합니다.

```bash
./gradlew spotlessApply
./gradlew spotlessCheck
```

GitHub Actions 설정은 [Android CI](.github/workflows/android-ci.yml)와 [포맷 검사](.github/workflows/format-check.yml)에 있습니다. 저장소 규칙상 통합·기본 브랜치는 `dev`, 릴리스 대상은 `main`입니다. Android 기반 설정 PR #2, Issue #3·#5의 화면 UI PR #4·#6, Issue #9의 Spotless·ktlint PR #10은 `dev`에 병합되었습니다. 전체 UI 리디자인은 [Issue #12](https://github.com/TEAM-SIRU/EcoGuard-APP/issues/12), `feature/#12-figma-full-ui-redesign` 브랜치에서 진행합니다. 기존 규칙 변경은 UI 변경과 별도 커밋으로 보존했습니다.

### 리디자인 검증 현황

구현 작업자의 실행 결과 기준으로 `spotlessApply`, `spotlessCheck`, `:app:testDebugUnitTest`, `:app:lintDebug`, `:app:assembleDebug`, `:app:assembleDebugAndroidTest`가 통과했습니다. 단위 테스트 4개가 통과했으며 Android Lint 결과는 오류 0개·경고 35개입니다.

62개 상태를 390×844dp(글자 배율 1.0), 320×596dp(글자 배율 1.5), 1280×900dp(글자 배율 1.0)에서 렌더링하고 원본 디자인과 시각 대조했습니다. 신청 입력의 Unicode 경계·외부 상태 동기화·접근성, 월 선택, 빈 상태 스크롤을 포함한 320dp 계측 테스트 6개 및 390dp·1280dp의 빈 상태·전체 렌더링 테스트 각 2개가 통과했습니다. 최종 변경 후 포맷·빌드·단위 테스트·Lint를 다시 실행해 통과했고 대표 캡처를 확인했습니다. 검증 후 기기 설정도 복원했습니다. 렌더링 테스트는 화면 생성·오류 여부를 검사하며 픽셀 차이를 자동 판정하지 않습니다. 상태별 대응과 검증 기준은 [리디자인 검증 문서](docs/redesign-verification.md)를 참고하세요.

대표 화면 캡처: [휴대폰 홈](docs/screenshots/phone-home.png), [신청](docs/screenshots/phone-application.png), [마이페이지](docs/screenshots/phone-menu.png), [태블릿 홈](docs/screenshots/tablet-home.png), [태블릿 청소구역](docs/screenshots/tablet-area.png).

## 프로젝트 구조

```text
EcoGuard-APP/
├── app/                         # Android 앱 모듈
│   └── src/
│       ├── main/                 # 앱 진입점, 화면별 UI(feature), 공통 컴포넌트·테마, 리소스
│       ├── test/                 # 로컬 단위 테스트
│       └── androidTest/          # 기기 계측 테스트
├── .github/                      # Issue/PR 템플릿 및 Actions workflow
├── gradle/libs.versions.toml     # 플러그인 및 라이브러리 버전
├── build.gradle.kts              # 루트 Gradle 플러그인 설정
└── settings.gradle.kts           # 프로젝트 및 모듈 설정
```

앱 실행 흐름은 `MainActivity` → `HomeRoute`이며, `HomeRoute`는 기본값인 `HomeUiState.Loading` 상태로 `HomeLoadingScreen`과 하단 탭을 표시합니다. 다른 화면은 아직 진입 경로가 없고 Preview로 확인합니다.

`app/src/main/java/com/nativelap/ecoguard/` 아래 구성은 다음과 같습니다.

- `feature/<기능>/view/`: Route, Screen, 화면 전용 Composable, Preview용 `*PreviewFixtures`(일부 기능만)
- `feature/<기능>/viewmodel/`: `*UiState`, `*ScreenEvent`, `*UiModel` 등 화면 상태 타입(`notice`는 없음). 이름과 달리 ViewModel 클래스는 아직 없습니다.
- `ui/component/`: 여러 화면이 공유하는 컴포넌트(버튼, 상단 바, 하단 탭, 다이얼로그, 바텀시트, 스켈레톤 등)
- `ui/theme/`: `Theme.kt`, `AppSpacing`, `AppRadius`, `AppComponentSize`, `AppIconSize`, `AppTypography`·`AppExtraTypography`, `AppExtraColors`

Route는 전달받은 고정 상태(기본값 또는 Preview 주입 값)만 표시하며, ViewModel이나 서버 데이터 흐름은 없습니다.

## 문서와 확인 항목

- 화면 상태 대응과 검증: [리디자인 상태 및 검증](docs/redesign-verification.md)
- 디자인 에셋과 출처: [Redesign 2 원본 에셋](docs/redesign-assets.md)
- PRD는 아직 없습니다. 앱의 목표, 대상 사용자, 기능 범위는 **확인 필요**입니다.
