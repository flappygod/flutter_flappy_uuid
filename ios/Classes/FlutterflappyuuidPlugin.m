#import "FlutterflappyuuidPlugin.h"
#import "SSFlappyKeychain.h"

@implementation FlutterflappyuuidPlugin
{
    NSString* uuid;
}
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    FlutterMethodChannel* channel = [FlutterMethodChannel
                                     methodChannelWithName:@"flutter_flappy_uuid"
                                     binaryMessenger:[registrar messenger]];
    FlutterflappyuuidPlugin* instance = [[FlutterflappyuuidPlugin alloc] init];
    [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    //获取UUID
    if ([@"getUUID" isEqualToString:call.method]) {
        //如果UUID是空的
        if(uuid!=nil){
            //返回
            result(uuid);
        }else{
            //获取UUID
            uuid=[self getIOSUUID];
            //成功
            result(uuid);
        }
    } else {
        result(FlutterMethodNotImplemented);
    }
}

- (NSString *)getIOSUUID
{
    //获取应用的UUID
    NSString *retrieveuuid = [SSFlappyKeychain passwordForService:[[NSBundle mainBundle]bundleIdentifier] account:@"uuid"];
    //如果是空的
    if ( retrieveuuid == nil || [retrieveuuid isEqualToString:@""]){
        //创建
        CFUUIDRef uuid = CFUUIDCreate(NULL);
        assert(uuid != NULL);
        //字符串
        CFStringRef uuidStr = CFUUIDCreateString(NULL, uuid);
        //大写
        retrieveuuid = [[NSString stringWithFormat:@"%@", uuidStr] uppercaseString];;
        //保存当前的
        [SSFlappyKeychain setPassword:retrieveuuid forService:[[NSBundle mainBundle]bundleIdentifier] account:@"uuid"];
    }
    return retrieveuuid;
}

@end
