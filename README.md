# EcoGuard

환경지킴이 앱 레포지토리입니다.

## 프로젝트 상태

현재 프로젝트는 Kotlin과 Jetpack Compose를 사용하는 Android 앱입니다. Navigation 3로 로그인과 주요 화면을 연결했고, DataGSM OAuth 로그인을 위한 Android client와 EcoGuard 세션 처리를 구현했습니다. 로그인은 서버에서 받은 인증 URL을 Custom Tab으로 열고, 검증된 App Link의 일회용 handoff 코드를 서버에 교환해 세션을 만듭니다. 서버 API 계층도 추가했지만 일반 기능 화면은 아직 샘플 데이터를 사용합니다. 제품 기능, 대상 사용자, 전체 범위는 PRD가 없어 **확인 필요**입니다.

### 디자인 및 구현 기준

[Issue #12](https://github.com/TEAM-SIRU/EcoGuard-APP/issues/12)에서 Figma [‘App Screens · Redesign 2’ 페이지 (823:574)](https://www.figma.com/design/Ef8bcly7z4kzYE4D7W9uXU/?node-id=823-574)의 58개 화면·상태를 반영했습니다. 추가 Preview 상태 4개를 포함해 62개 상태를 렌더링 검증 대상으로 관리합니다.

- 글꼴: Noto Sans KR Regular·Medium·Bold를 `res/font`에서 사용합니다. [OFL 라이선스](licenses/NotoSansKR-OFL.txt)를 포함합니다.
- 색상: Primary `#55B580`, 기본 CTA에 사용하는 Secondary `#30875B`, 배경 `#FAFBFA`, 흰색 보조 버튼과 테두리를 적용합니다. CTA 색상은 Figma 원본 PNG의 실제 색상을 기준으로 합니다.
- 수치: 기본 CTA 높이 48dp, FAB 크기 60dp 등 Figma 원본 크기·간격을 디자인 토큰과 화면에 반영합니다.
- 공통 UI 보정: [Issue #14](https://github.com/TEAM-SIRU/EcoGuard-APP/issues/14)에서 화면별 원본 노드의 크기·간격·문구를 재대조했습니다. 작은 액션은 시각 크기를 유지하면서 터치 영역을 최소 48dp로 확장하며, 다이얼로그·키보드·긴 콘텐츠의 스크롤 접근성을 보완합니다.
- 에셋: 원본 SVG를 Android VectorDrawable로 변환하고 청소구역 도면을 로컬 이미지로 사용합니다. 원본·리소스 매핑은 [에셋 문서](docs/redesign-assets.md)를 참고하세요.
- 신청 입력: 학번·이름은 읽기 전용입니다. 동기는 Unicode 코드 포인트 기준 최대 200자로 제한하고 카운터를 표시합니다. 빈 내용·공백만 있는 내용·제출 중에는 신청 버튼을 비활성화합니다. 입력은 저장 가능한 Compose 상태로 유지하며 외부 상태 변경을 반영합니다.
- 반응형: 기본 콘텐츠 최대폭은 600dp입니다. 홈·청소구역은 840dp 이상에서 2열·최대폭 960dp를 사용하며, 나머지 화면은 최대폭 600dp를 유지합니다.
- 테마: 밝은 테마를 사용합니다. 다크 디자인 확정 전까지 다크 색상 구성도 밝은 테마와 같습니다.
- 범위: 기존 Route·Screen·UiState·화면 이벤트 구조를 유지합니다. 인증은 DataGSM OAuth client와 세션 계층을 사용하며, 일반 기능 화면의 데이터 요청과 실제 카메라 촬영은 아직 연결하지 않았습니다.

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
| `notice` | 공지 목록·상세, 빈 상태·로딩·불러오기 실패·누락된 공지 안내 |
| `notification` | 알림 목록·읽음 상태, 관련 상세 이동, 빈 상태·로딩·불러오기 실패 |
| `demo` | 샘플 데이터·입력·제출 상태 및 화면 이벤트 처리 |

앱 시작 시 시스템 스플래시(흰 배경과 새싹 아이콘)를 표시합니다. 플랫폼 제약으로 Figma의 '환경지킴이' 글자는 스플래시에 표시하지 않습니다.

OAuth staging 로그인은 서버와 도메인 설정이 완료되어야 검증할 수 있습니다. 서버의 `POST /api/v1/auth/authorize`, `POST /api/v1/auth/exchange`, callback의 `handoff_challenge` 전달 계약과 앱 링크 도메인의 `assetlinks.json`은 아직 배포·검증되지 않았습니다. 이 조건이 갖춰지기 전까지 end-to-end 로그인을 확인할 수 없습니다. 일반 기능 화면의 실제 데이터 연결, 실제 촬영, 이용안내는 후속 작업이며 일정은 **확인 필요**입니다.

### 인증 및 서버 연동

- 인증 시작: 앱이 `POST /api/v1/auth/authorize`에 PKCE S256 challenge를 보내고, 응답의 DataGSM 인증 URL을 Android Custom Tab으로 엽니다.
- callback handoff: 서버 callback이 DataGSM 인증을 처리한 뒤 `https://ecoguard.https.gsmsv.site/auth/mobile-callback` App Link로 일회용 `handoff_code`와 이를 요청한 `handoff_challenge`를 전달하는 계약을 전제로 합니다. 앱은 challenge가 현재 로그인 시도와 일치하는지 확인한 뒤 `POST /api/v1/auth/exchange`로 코드를 교환합니다.
- 세션: Access/Refresh Token은 Android Keystore로 보호되는 암호화 저장소에 보관하고, `SessionManager`가 세션 상태와 토큰 제공을 담당합니다. 앱 시작 시 저장된 세션을 복원합니다.
- 보호 API 요청은 공통 Bearer interceptor를 사용합니다. 인증 실패 시 unauthenticated Retrofit으로 refresh를 한 번 수행하고, 성공하면 원 요청을 재시도합니다. Refresh 실패 시 로컬 세션을 만료시킵니다.
- 로그아웃은 로컬 세션을 먼저 제거하고 서버 logout API를 호출합니다. 서버 요청 실패 여부는 앱 상태에서 별도로 처리합니다.
- `authorize`·`exchange`·callback handoff는 현재 서버 Swagger에 계약이 완전히 정의되어 있지 않은 client 측 전제입니다. 서버 구현 및 Android App Link 도메인의 서명 지문을 포함한 `assetlinks.json` 배포가 필요합니다.

## 기술 스택

| 항목 | 설정 |
|---|---|
| 모듈 | `:app` |
| 언어 및 UI | Kotlin 2.2.10, Jetpack Compose, Material 3 |
| 내비게이션·목적지 직렬화 | Navigation 3 1.1.6, Kotlin Serialization 1.9.0 |
| 서버 통신·인증 | Retrofit 3.0.0, OkHttp 5.5.0, kotlinx.serialization, AndroidX Browser Custom Tabs 1.10.0 |
| 의존성 주입 | Hilt 2.60.1 |
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

오프라인 검증 명령은 다음과 같습니다. 현재 내비게이션 변경의 실행 결과는 [내비게이션 검증 문서](docs/navigation-verification.md)를 참고하세요.

```bash
./gradlew --offline :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

포맷 적용과 검사는 다음 명령을 사용합니다.

```bash
./gradlew spotlessApply
./gradlew spotlessCheck
```

GitHub Actions 설정은 [Android CI](.github/workflows/android-ci.yml)와 [포맷 검사](.github/workflows/format-check.yml)에 있습니다. 저장소 규칙상 통합·기본 브랜치는 `dev`, 릴리스 대상은 `main`입니다. Android 기반 설정 PR #2, Issue #3·#5의 화면 UI PR #4·#6, Issue #9의 Spotless·ktlint PR #10은 `dev`에 병합되었습니다. 전체 UI 리디자인은 [Issue #12](https://github.com/TEAM-SIRU/EcoGuard-APP/issues/12)에서 관리했습니다. 후속 공통 UI 보정은 [Issue #14](https://github.com/TEAM-SIRU/EcoGuard-APP/issues/14), `fix/#14-redesign-ui-corrections` 브랜치에서 진행합니다. 기존 규칙 변경은 UI 변경과 별도 커밋으로 보존했습니다.

### 리디자인 검증 현황

2026-10-12 Issue #14의 Figma UI 보정(UI-01~UI-15)을 완료했습니다. Spotless, Debug 앱·계측 APK 빌드, 단위 테스트와 Android Lint가 통과했습니다. Lint 오류는 0개, 경고는 31개였습니다.

- 세로 화면 390×844dp/font 1.0, 320×596dp/font 1.0·1.5, 430×932dp/font 1.0·1.5에서 각각 계측 테스트 18개와 62개 화면 상태 Preview 렌더가 통과했습니다. 조건별 홈 스크롤 캡처도 확인했습니다.
- 기본 크기 변경 화면을 Figma 원본과 수동 대조했고, 320×596dp/font 1.5에서 작은 화면의 대표 상태를 확인했습니다. Preview 검증은 자동 픽셀 일치 판정이 아닙니다.
- 계측에 사용한 화면 크기·density·글자 배율·키보드·회전 설정은 실행 전 상태로 복원했습니다. 서버 연동·실제 촬영·가로 화면 동작은 정적 UI 검증 범위에 포함하지 않았습니다.

전체 상태 대응표와 이전 Issue #12·#14 검증 이력은 [리디자인 검증 문서](docs/redesign-verification.md)를 참고하세요.

대표 화면 캡처: [휴대폰 홈](docs/screenshots/phone-home.png), [신청](docs/screenshots/phone-application.png), [마이페이지](docs/screenshots/phone-menu.png), [태블릿 홈](docs/screenshots/tablet-home.png), [태블릿 청소구역](docs/screenshots/tablet-area.png).

추가 보정 캡처: [홈 전후 비교와 검증](docs/redesign-verification.md#보정-전후와-실행-캡처), [활동 기록](docs/screenshots/phone-activity.png), [다이얼로그](docs/screenshots/phone-dialog.png), [홈 마지막 기록](docs/screenshots/phone-home-scrolled.png), [실제 앱 시작](docs/screenshots/app-start-loading.png).

### 내비게이션 검증 현황

2026-10-13 Issue #17에서 Spotless, Debug 앱·계측 APK 빌드, 단위 테스트 11개와 Android Lint가 통과했습니다. Lint 오류는 0개, 경고는 29개였습니다.

- 기본 크기 내비게이션·렌더 계측 10개와 기존 입력·IME·터치·다이얼로그를 포함한 회귀 계측 25개가 통과했습니다.
- 320×596dp/font 1.5에서 내비게이션 계측 8개와 렌더 계측 2개, 840×1200dp/font 1.0에서 렌더 계측 2개가 통과했습니다. 크기별로 41개 목적지·신규 상태를 캡처했습니다.
- reviewer·security_reviewer 검토에서 확인된 Critical·Warning은 없었고, designer의 세 크기 캡처 감사에서 추가 수정 요구가 없었습니다. 취약점 DB 스캔과 실기기 검증은 미실행입니다.

캡처: [알림 목록](docs/screenshots/navigation-notifications.png), [공지 목록](docs/screenshots/navigation-notices.png), [공지 상세](docs/screenshots/navigation-notice-detail.png), [작은 화면·큰 글자 다이얼로그](docs/screenshots/navigation-small-dialog.png), [태블릿 공지 목록](docs/screenshots/navigation-tablet-notices.png).

실행 조건과 검증 한계는 [내비게이션 구현 및 검증](docs/navigation-verification.md)에 기록합니다.

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

앱 실행 흐름은 `MainActivity` → `EcoGuardNavHost`이며, `AuthViewModel`이 세션 상태를 관찰해 인증 전·후 목적지를 전환합니다. 로그인 성공 세션은 앱 재시작 후 복원됩니다. 일반 기능 화면은 `DemoDestinationRoute`와 `DemoFlowController`가 샘플 상태를 주입하고, 서버 API 데이터와 연결되지 않았습니다. 로그아웃은 실제 세션을 로컬에서 종료하고 서버 logout API를 요청합니다. 회원탈퇴는 확인 UI만 있으며 서버 작업은 연결되지 않았습니다.

### 샘플 내비게이션

- 홈·청소구역·활동 기록·마이페이지는 각각 독립된 back stack을 유지합니다. 탭을 다시 선택해도 현재 화면을 유지하며 저장 가능한 입력·스크롤 상태를 보존하도록 구성합니다.
- 중앙 카메라 버튼은 현재 탭에서 인증 안내 → 샘플 촬영 → 사진 확인 → 제출 완료로 이동합니다. 재촬영은 샘플 촬영으로 돌아갑니다.
- 모집 → 신청 → 신청 결과, 활동 기록 → 인증 결과·상세 → 이의신청 → 제출 완료, 마이페이지 → 이의신청 내역·결과를 연결합니다. 제출 완료 시 입력 단계를 back stack에서 정리합니다.
- 홈 알림 아이콘은 알림 목록으로, 알림 항목은 관련 상세로 이동합니다. 홈 공지 카드는 공지 상세로, 마이페이지 공지는 공지 목록으로 이동합니다.
- 월 선택·로그아웃·회원탈퇴 다이얼로그와 인증 불가 시트도 목적지로 관리합니다. 뒤로가기는 현재 목적지를 닫고, 홈 이외 탭의 첫 화면에서는 홈으로 돌아갑니다. 홈 첫 화면에서는 앱을 종료합니다.
- 이용안내는 후속 작업이며 버튼을 비활성화합니다. 청소 알림 설정도 샘플 상태만 변경합니다.

내비게이션 변경은 [Issue #17](https://github.com/TEAM-SIRU/EcoGuard-APP/issues/17), `feature/#17-navigation` 브랜치에서 진행합니다. 빌드·테스트·화면별 검증 결과는 [내비게이션 검증 문서](docs/navigation-verification.md)를 참고하세요.

`app/src/main/java/com/nativelap/ecoguard/` 아래 구성은 다음과 같습니다.

- `feature/<기능>/view/`: Route, Screen, 화면 전용 Composable, Preview용 `*PreviewFixtures`(일부 기능만)
- `feature/auth/presentation/`: OAuth 로그인, handoff, 세션 상태를 다루는 `AuthViewModel`과 `AuthUiState`.
- `feature/<기능>/viewmodel/`: 화면 상태 타입과 샘플 이벤트. `demo`에는 저장 가능한 샘플 앱 상태가 있습니다. 인증 외 기능의 ViewModel 클래스와 서버 데이터 연결은 아직 없습니다.
- `feature/auth/data/`, `feature/auth/domain/`: 인증 API DTO·서비스·RemoteDataSource·Repository·UseCase, 암호화 토큰 저장과 세션 관리.
- `core/network/`: Retrofit·OkHttp 설정, 공통 API 결과/오류 처리, Bearer 주입과 토큰 갱신.
- `navigation/`: 직렬화 가능한 목적지, 인증 전·후 NavDisplay, 탭별 back stack·Navigator, 바텀시트 SceneStrategy
- `ui/component/`: 여러 화면이 공유하는 컴포넌트(버튼, 상단 바, 하단 탭, 다이얼로그, 바텀시트, 스켈레톤 등)
- `ui/theme/`: `Theme.kt`, `AppSpacing`, `AppRadius`, `AppComponentSize`, `AppIconSize`, `AppTypography`·`AppExtraTypography`, `AppExtraColors`

기능별 Route·Screen은 전달받은 UiState와 화면 이벤트를 사용합니다. 인증은 `AuthViewModel`과 인증 Domain/Data 계층이 OAuth 흐름 및 세션을 처리합니다. 나머지 화면은 `DemoDestinationRoute`와 `DemoFlowController`가 샘플 상태를 주입하고 이벤트를 화면 이동으로 연결합니다. `DemoAppState`는 샘플 입력·제출·알림 읽음 상태를 저장하며, Navigator는 back stack 조작을 담당합니다. `StateRestorationTester`로 신청·이의신청 입력, 알림 읽음 상태, 월 선택과 탭별 스택의 저장·복원을 확인했습니다. 실제 `MainActivity`에서도 백그라운드 프로세스 종료 후 공지 상세 복원과 뒤로가기(알림 → 홈)를 확인했습니다. 자세한 실행 조건은 [실제 프로세스 복원 검증](docs/navigation-verification.md#실제-프로세스-복원)을 참고하세요.

## 문서와 확인 항목

- 내비게이션 흐름과 검증: [Navigation 3 구현 및 검증](docs/navigation-verification.md)
- 화면 상태 대응과 검증: [리디자인 상태 및 검증](docs/redesign-verification.md)
- 디자인 에셋과 출처: [Redesign 2 원본 에셋](docs/redesign-assets.md)
- PRD는 아직 없습니다. 앱의 목표, 대상 사용자, 기능 범위는 **확인 필요**입니다.
