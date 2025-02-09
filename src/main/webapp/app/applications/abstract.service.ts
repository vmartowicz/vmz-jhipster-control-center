import { Route } from '@/shared/routes/routes.service';

export default abstract class AbstractService {
  private GATEWAY_PATH = 'gateway/';

  protected generateUri(route: Route, basePath: string, ...paths: string[]): string {
    const controlCenterUri = (SERVER_API_URL !== undefined ? SERVER_API_URL : '') + basePath;
    const instanceUri = this.GATEWAY_PATH + route.path + basePath;

    return (route?.path?.length > 0 ? instanceUri : controlCenterUri) + paths.join('/');
  }
}
