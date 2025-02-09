import Component from 'vue-class-component';
import { Inject, Vue } from 'vue-property-decorator';
import LoginService from '@/account/login.service';

@Component
export default class Home extends Vue {
  @Inject('loginService')
  private loginService: () => LoginService;

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

  public get username(): string {
    return this.$store.getters.account?.login ?? '';
  }
}
