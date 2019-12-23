#import <Cordova/CDV.h>

@interface IamportCordova : CDVPlugin {
  // Member variables go here.
}

- (void)coolMethod:(CDVInvokedUrlCommand*)command;
- (void)startActivity: (CDVInvokedUrlCommand*)command;
@end