import { Component, Inject, Vue } from 'vue-property-decorator';
import LoginService from '@/account/login.service';
import AccountService from '@/account/account.service';

@Component
export default class JhiNavbar extends Vue {
  @Inject('loginService')
  private loginService: () => LoginService;

  @Inject('accountService') private accountService: () => AccountService;
  public version = 'v' + VERSION;
  private currentLanguage = this.$store.getters.currentLanguage;
  private languages: any = this.$store.getters.languages;
  private hasAnyAuthorityValues = {};

  created() {}

  public subIsActive(input) {
    const paths = Array.isArray(input) ? input : [input];
    return paths.some(path => {
      return this.$route.path.indexOf(path) === 0; // current path starts with this path string
    });
  }

  /*public logout_(): Promise<any> {
    localStorage.removeItem('jhi-authenticationToken');
    sessionStorage.removeItem('jhi-authenticationToken');
    this.$store.commit('logout');
    if (this.$route.path !== '/') {
      return this.$router.push('/');
    }
    return Promise.resolve(this.$router.currentRoute);
  }*/

  // jhcc-custom
  public logout(): Promise<any> {
    if (this.$store.getters.activeProfiles.includes('oauth2')) {
      return this.loginService()
        .logout()
        .then(response => {
          this.$store.commit('logout');
          this.$router.push('/');
          const data = response.data;
          let logoutUrl = data.logoutUrl;
          // if Keycloak, uri has protocol/openid-connect/token
          if (logoutUrl.indexOf('/protocol') > -1) {
            logoutUrl = logoutUrl + '?redirect_uri=' + window.location.origin;
          } else {
            // Okta
            logoutUrl = logoutUrl + '?id_token_hint=' + data.idToken + '&post_logout_redirect_uri=' + window.location.origin;
          }
          window.location.href = logoutUrl;
        });
    } else {
      localStorage.removeItem('jhi-authenticationToken');
      sessionStorage.removeItem('jhi-authenticationToken');
      this.$store.commit('logout');
      return this.$router.push('/', () => {});
    }
  }

  // jhcc-custom
  public openLogin(): void {
    if (this.$store.getters.activeProfiles.includes('oauth2')) {
      this.loginService().login();
    } else {
      this.loginService().openLogin((<any>this).$root);
    }
  }

  public get authenticated(): boolean {
    return this.$store.getters.authenticated;
  }

  public hasAnyAuthority(authorities: any): boolean {
    this.accountService()
      .hasAnyAuthorityAndCheckAuth(authorities)
      .then(value => {
        if (this.hasAnyAuthorityValues[authorities] !== value) {
          this.hasAnyAuthorityValues = { ...this.hasAnyAuthorityValues, [authorities]: value };
        }
      });
    return this.hasAnyAuthorityValues[authorities] ?? false;
  }

  public get openAPIEnabled(): boolean {
    return this.$store.getters.activeProfiles.indexOf('api-docs') > -1;
  }

  public get inProduction(): boolean {
    return this.$store.getters.activeProfiles.indexOf('prod') > -1;
  }
}
